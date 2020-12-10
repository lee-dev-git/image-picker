package a.alt.z.imagepicker.util

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

fun ConstraintLayout.update(
        constraintSet: ConstraintSet = ConstraintSet(),
        update: ConstraintSet.() -> Unit
) = constraintSet.apply {
    clone(this@update)
    update()
    applyTo(this@update)
}

fun ConstraintLayout.updateTransition(
        constraintSet: ConstraintSet = ConstraintSet(),
        duration: Long = 250L,
        interpolator: Interpolator = AccelerateDecelerateInterpolator(),
        update: ConstraintSet.() -> Unit
) {
    update(constraintSet, update)
    TransitionManager.beginDelayedTransition(this, ChangeBounds().apply { setDuration(duration); setInterpolator(interpolator) })
}

val Guideline.guidePercent get() = (layoutParams as ConstraintLayout.LayoutParams).guidePercent