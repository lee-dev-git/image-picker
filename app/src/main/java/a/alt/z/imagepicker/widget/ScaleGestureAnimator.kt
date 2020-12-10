package a.alt.z.imagepicker.widget

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator

class ScaleGestureAnimator constructor(
        private val targetView: View,
        private val maxScale: Float,
        private val animatorX: ObjectAnimator,
        private val animatorY: ObjectAnimator
) : ScaleAnimator {

    constructor(
            targetView: View,
            maxScale: Float
    ) : this(
    targetView = targetView,
    maxScale = maxScale,
    animatorX = ANIMATOR_X,
    animatorY = ANIMATOR_Y
    )

    init {
        animatorX.target = targetView
        animatorY.target = targetView
    }

    override fun scale(scale: Float) {
        animatorX.cancel()
        animatorX.clearProperties()
        animatorX.setFloatValues(targetView.scaleX * scale)
        animatorX.start()

        animatorY.cancel()
        animatorY.clearProperties()
        animatorY.setFloatValues(targetView.scaleY * scale)
        animatorY.start()
    }

    override fun adjust() {
        if (targetView.scaleX < ScaleAnimator.ORIGINAL_SCALE) {
            animatorX.cancel()
            animatorX.setupProperties()
            animatorX.setFloatValues(ScaleAnimator.ORIGINAL_SCALE)
            animatorX.start()
        } else if (maxScale < targetView.scaleX) {
            animatorX.cancel()
            animatorX.setupProperties()
            animatorX.setFloatValues(maxScale)
            animatorX.start()
        }

        if (targetView.scaleY < ScaleAnimator.ORIGINAL_SCALE) {
            animatorY.cancel()
            animatorY.setupProperties()
            animatorY.setFloatValues(ScaleAnimator.ORIGINAL_SCALE)
            animatorY.start()
        } else if (maxScale < targetView.scaleY) {
            animatorY.cancel()
            animatorY.setupProperties()
            animatorY.setFloatValues(maxScale)
            animatorY.start()
        }
    }

    private fun ObjectAnimator.clearProperties() {
        duration = 0
        interpolator = null
    }

    private fun ObjectAnimator.setupProperties() {
        duration = ScaleAnimator.ADJUSTING_DURATION
        interpolator = DecelerateInterpolator(ScaleAnimator.ADJUSTING_FACTOR)
    }

    companion object {
        private val ANIMATOR_X = ObjectAnimator().apply { setProperty(View.SCALE_X) }
        private val ANIMATOR_Y = ObjectAnimator().apply { setProperty(View.SCALE_Y) }
    }
}
