package a.alt.z.imagepicker.widget

import android.animation.ObjectAnimator
import android.graphics.Rect
import android.view.View
import androidx.dynamicanimation.animation.*

class VerticalMoveAnimator constructor(
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
            topBound: Float,
            bottomBound: Float,
            maxScale: Float
    ) : this(
            targetView = targetView,
            maxScale = maxScale,
            spring = SpringAnimation(targetView, HORIZONTAL_PROPERTY).setSpring(SPRING_FORCE),
            fling = FlingAnimation(targetView, DynamicAnimation.Y).setFriction(MoveAnimator.FRICTION),
            animator = ANIMATOR
    ) {
        startBound = topBound
        endBound = bottomBound
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
        animator.setFloatValues(targetView.translationY + delta)
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
            maxScale < targetView.scaleY -> {
                val heightDiff = ((targetRect.height() - targetRect.height() * (maxScale / targetView.scaleY)) / 2).toInt()
                val widthDiff = ((targetRect.width() - targetRect.width() * (maxScale / targetView.scaleY)) / 2).toInt()
                Rect(
                        targetRect.left + widthDiff,
                        targetRect.top + heightDiff,
                        targetRect.right - widthDiff,
                        targetRect.bottom - heightDiff
                )
            }
            targetView.scaleY < 1f -> {
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
        return startBound < rect.top || rect.bottom < endBound
    }

    private fun adjustToBounds(rect: Rect, velocity: Float = 0f) {
        val scale = when {
            maxScale < targetView.scaleX -> maxScale
            targetView.scaleX < 1f -> 1f
            else -> targetView.scaleX
        }
        val diff = (targetView.height * scale - targetView.height) / 2
        if (startBound < rect.top) {
            cancel()
            val finalPosition = startBound + diff
            spring.setStartVelocity(velocity).animateToFinalPosition(finalPosition)
        } else if (rect.bottom < endBound) {
            cancel()
            val finalPosition = endBound - targetView.height.toFloat() - diff
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
            setProperty(View.TRANSLATION_Y)
            interpolator = null
            duration = 0
        }

        private val HORIZONTAL_PROPERTY = object : FloatPropertyCompat<View>("Y") {
            override fun getValue(view: View): Float {
                return view.y
            }

            override fun setValue(view: View, value: Float) {
                view.y = value
            }
        }

        private val SPRING_FORCE = SpringForce().setStiffness(MoveAnimator.STIFFNESS).setDampingRatio(MoveAnimator.DAMPING_RATIO)
    }
}