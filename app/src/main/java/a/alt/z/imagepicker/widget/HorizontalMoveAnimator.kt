package a.alt.z.imagepicker.widget

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import androidx.dynamicanimation.animation.*

class HorizontalMoveAnimator constructor(
    private val targetView: View,
    private val maxScale: Float,
    private val spring: SpringAnimation,
    private val fling: FlingAnimation,
    private val animator: ObjectAnimator
) : MoveAnimator {

    override var startBound: Float = 0F

    override var endBound: Float = 0F

    constructor(
        targetView: View,
        leftBound: Float,
        rightBound: Float,
        maxScale: Float
    ) : this(
        targetView = targetView,
        maxScale = maxScale,
        spring = SpringAnimation(targetView, VERTICAL_PROPERTY).setSpring(SPRING_FORCE),
        fling = FlingAnimation(targetView, DynamicAnimation.X).setFriction(MoveAnimator.FRICTION),
        animator = ANIMATOR
    ) {
        startBound = leftBound
        endBound = rightBound
    }

    private val updateListener = DynamicAnimation.OnAnimationUpdateListener { _, _, velocity ->
        val expectedRect = expectRect()
        if (outOfBounds(expectedRect)) {
            adjustToBounds(expectedRect, velocity)
        }
    }

    init {
        animator.target = targetView
    }

    override fun move(delta: Float) {
        cancel()
        animator.setFloatValues(targetView.translationX + delta)
        animator.start()
    }

    override fun adjust() {
        val expectedRect = expectRect()
        if (outOfBounds(expectedRect)) {
            adjustToBounds(expectedRect)
        }
    }

    override fun fling(velocity: Float) {
        cancel()
        fling.addUpdateListener(updateListener)
        fling.setStartVelocity(velocity).start()
    }

    private fun expectRect(): Rect {
        val targetRect = Rect()
        targetView.getHitRect(targetRect)
        return when {
            maxScale < targetView.scaleX -> {
                val heightDiff = ((targetRect.height() - targetRect.height() * (maxScale / targetView.scaleY)) / 2).toInt()
                val widthDiff = ((targetRect.width() - targetRect.width() * (maxScale / targetView.scaleY)) / 2).toInt()
                Rect(
                    targetRect.left + widthDiff,
                    targetRect.top + heightDiff,
                    targetRect.right - widthDiff,
                    targetRect.bottom - heightDiff
                )
            }
            targetView.scaleX < 1f -> {
                val heightDiff = (targetView.height - targetRect.height()) / 2
                val widthDiff = (targetView.width - targetRect.width()) / 2
                Rect(
                    targetRect.left + widthDiff,
                    targetRect.top + heightDiff,
                    targetRect.right - widthDiff,
                    targetRect.bottom - heightDiff
                )
            }
            else -> targetRect
        }
    }

    private fun outOfBounds(rect: Rect): Boolean {
        return startBound < rect.left || rect.right < endBound
    }

    private fun adjustToBounds(rect: Rect, velocity: Float = 0f) {
        val scale = when {
            maxScale < targetView.scaleX -> maxScale
            targetView.scaleX < 1f -> 1f
            else -> targetView.scaleX
        }
        val diff = (targetView.width * scale - targetView.width) / 2

        if (startBound < rect.left) {
            cancel()
            val finalPosition = startBound + diff
            spring.setStartVelocity(velocity).animateToFinalPosition(finalPosition)
        } else if (rect.right < endBound) {
            cancel()
            val finalPosition = endBound - targetView.width.toFloat() - diff
            spring.setStartVelocity(velocity).animateToFinalPosition(finalPosition)
        }
    }

    private fun cancel() {
        animator.cancel()
        spring.cancel()
        fling.cancel()
        fling.removeUpdateListener(updateListener)
    }

    companion object {

        private val ANIMATOR = ObjectAnimator().apply {
            setProperty(View.TRANSLATION_X)
            interpolator = null
            duration = 0
        }

        private val VERTICAL_PROPERTY = object : FloatPropertyCompat<View>("X") {
            override fun getValue(view: View): Float {
                return view.x
            }

            override fun setValue(view: View, value: Float) {
                view.x = value
            }
        }

        private val SPRING_FORCE = SpringForce().setStiffness(MoveAnimator.STIFFNESS).setDampingRatio(
            MoveAnimator.DAMPING_RATIO
        )
    }
}