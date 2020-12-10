package a.alt.z.imagepicker.widget

import a.alt.z.imagepicker.databinding.CropLayoutBinding
import a.alt.z.imagepicker.util.layoutInflater
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnPreDraw
import timber.log.Timber
import timber.log.debug

/**
 * constraint layout
 *  properties
 *  - ratio
 *  - image view scale
 *  - image view visible rect
 *  - save / restore
 */
class CropLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val cropImageView: CropImageView

    private val cropOverlay: CropOverlay

    var frame: RectF? = null

    init {
        cropImageView = CropImageView(context, null, 0)
        cropImageView.apply {
            scaleType = ImageView.ScaleType.FIT_XY
            adjustViewBounds = true
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
        }

        cropOverlay = CropOverlay(context, null, 0)
        cropOverlay.apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, Gravity.CENTER)
        }

        addView(cropImageView, 0)
        addView(cropOverlay, 1)

        doOnPreDraw {
            val width = measuredWidth.toFloat()
            val height = measuredHeight.toFloat()
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28F, context.resources.displayMetrics)
            val frame = RectF(0F, 0F, width, height)
            this.frame = frame

            cropImageView.frame = frame
            cropImageView.requestLayout()

            cropOverlay.frame = frame
            cropOverlay.requestLayout()

            val animator = GestureAnimator.of(cropImageView, frame, 2F)
            val animation = GestureAnimation(cropOverlay, animator)
            animation.start()
        }

        /*
        viewTreeObserver.addOnPreDrawListener {
            val width = measuredWidth.toFloat()
            val height = measuredHeight.toFloat()
            val margin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 28F, context.resources.displayMetrics)
            val frame = RectF(margin, margin, width - margin, height - margin)
            this.frame = frame

            cropImageView.frame = frame
            cropImageView.requestLayout()

            cropOverlay.frame = frame
            cropOverlay.requestLayout()

            val animator = GestureAnimator.of(cropImageView, frame, 2F)
            val animation = GestureAnimation(cropOverlay, animator)
            animation.start()

            true
        }
        */
    }

    fun setUri(uri: Uri) {
        cropImageView.translationX = 0F
        cropImageView.translationY = 0F
        cropImageView.scaleX = 1F
        cropImageView.scaleY = 1F
        cropImageView.setImageURI(uri)
        cropImageView.requestLayout()
    }

    fun setUri(uri: Uri, param: CropParam) {

    }

    fun crop(): CropResult? {
        val frame = frame ?: return null
        val targetRect = Rect().apply { cropImageView.getHitRect(this) }
        val src = (cropImageView.drawable as BitmapDrawable).bitmap

        val bitmap = Bitmap.createScaledBitmap(src, targetRect.width(), targetRect.height(), false)
        val leftOffset = frame.left - targetRect.left
        val topOffset = frame.top - targetRect.top
        val width = frame.width().toInt()
        val height = frame.height().toInt()

        val result = Bitmap.createBitmap(bitmap, leftOffset.toInt(), topOffset.toInt(), width, height)

        return CropResult(src, result, scaleX, scaleY)
    }

    private fun cropInternal(src: Bitmap, scaleRect: Rect, cropRect: Rect) {
        val bitmap = Bitmap.createScaledBitmap(src, scaleRect.width(), scaleRect.height(), false)
        val result = Bitmap.createBitmap(bitmap, cropRect.left, cropRect.top, cropRect.width(), cropRect.height())
    }

    fun getCropParam(): CropParam? {
        cropImageView.drawable ?: return null

        val frame = frame ?: return null
        val scaleRect = Rect().apply { cropImageView.getHitRect(this) }

        val leftOffset = (frame.left - scaleRect.left).toInt()
        val topOffset = (frame.top - scaleRect.top).toInt()
        val width = frame.width().toInt()
        val height = frame.height().toInt()

        val cropRect = Rect(leftOffset, topOffset, leftOffset + width, topOffset + height)

        return CropParam(scaleRect, cropRect)
    }

    fun cropWithParam(cropParam: CropParam): Bitmap {
        val src = (cropImageView.drawable as BitmapDrawable).bitmap
        val bitmap = Bitmap.createScaledBitmap(src, cropParam.scaleRect.width(), cropParam.scaleRect.height(), false)
        val result = Bitmap.createBitmap(bitmap, cropParam.cropRect.left, cropParam.cropRect.top, cropParam.cropRect.width(), cropParam.cropRect.height())
        return result
    }
}

data class CropParam(
        val scaleRect: Rect,
        val cropRect: Rect
)

data class CropResult(
        val src: Bitmap,
        val result: Bitmap,
        val scaleX: Float,
        val scaleY: Float
)