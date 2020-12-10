package a.alt.z.imagepicker.widget

import android.graphics.RectF
import android.view.View

internal class GestureAnimator(
    private val horizontalAnimator: MoveAnimator,
    private val verticalAnimator: MoveAnimator,
    private val scaleAnimator: ScaleAnimator
) : ActionListener {

    override fun onScaled(scale: Float) {
        scaleAnimator.scale(scale)
    }

    override fun onScaleEnded() {
        scaleAnimator.adjust()
    }

    override fun onMoved(dx: Float, dy: Float) {
        horizontalAnimator.move(dx)
        verticalAnimator.move(dy)
    }

    override fun onFlinged(velocityX: Float, velocityY: Float) {
        horizontalAnimator.fling(velocityX)
        verticalAnimator.fling(velocityY)
    }

    override fun onMoveEnded() {
        horizontalAnimator.adjust()
        verticalAnimator.adjust()
    }

    companion object {
        fun of(target: View, frame: RectF, maxScale: Float): GestureAnimator {
            val horizontalAnimator = HorizontalMoveAnimator(
                targetView = target,
                leftBound = frame.left,
                rightBound = frame.right,
                maxScale = maxScale
            )
            val verticalAnimator = VerticalMoveAnimator(
                targetView = target,
                topBound = frame.top,
                bottomBound = frame.bottom,
                maxScale = maxScale
            )
            val scaleAnimator = ScaleGestureAnimator(
                targetView = target,
                maxScale = maxScale
            )

            return GestureAnimator(horizontalAnimator, verticalAnimator, scaleAnimator)
        }
    }
}