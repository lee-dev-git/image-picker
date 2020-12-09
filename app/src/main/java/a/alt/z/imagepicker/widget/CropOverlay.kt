package a.alt.z.imagepicker.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class CropOverlay @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint().apply {
        /* TODO */
        color = ContextCompat.getColor(context, android.R.color.black)
    }

    private val cropPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    var frame: RectF? = null

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawCrop(canvas)
        /* border? */
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRect(0F, 0F, width.toFloat(), height.toFloat(), backgroundPaint)
    }

    private fun drawCrop(canvas: Canvas) {
        val frameRect = frame ?: return
        val frameWidth = frameRect.width()
        val frameHeight = frameRect.height()

        val left = (width - frameWidth) / 2F
        val top = (height - frameHeight) / 2F
        val right = (width - frameWidth) / 2F
        val bottom = (height - frameHeight) / 2F

        canvas.drawRect(left, top, right, bottom, cropPaint)
    }
}