package a.alt.z.imagepicker.widget

import androidx.dynamicanimation.animation.SpringForce

internal interface MoveAnimator {

    /**
     * Move image
     * @param delta distance of how much image moves
     */
    fun move(delta: Float)

    /**
     * adjust image when image is off the frame
     */
    fun adjust()

    /**
     * fling image
     * @param velocity velocity when starting to fling
     */
    fun fling(velocity: Float)

    companion object {

        /**
         * stiffness when flinging or bouncing
         */
        const val STIFFNESS = SpringForce.STIFFNESS_VERY_LOW

        /**
         * dumping ratio when flinging or bouncing
         */
        const val DAMPING_RATIO = SpringForce.DAMPING_RATIO_LOW_BOUNCY

        /**
         * friction when flinging
         */
        const val FRICTION = 3f
    }
}
