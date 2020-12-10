package a.alt.z.imagepicker.widget

import android.content.Context
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class CropImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    var frame: RectF? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight.toFloat()
        val width = measuredWidth.toFloat()
        val frameRect = frame
        if(frameRect != null) {
            val widthScale = frameRect.width() / width
            val heightScale = frameRect.height() / height
            val scale = maxOf(widthScale, heightScale)
            setMeasuredDimension((width * scale).toInt(), (height * scale).toInt())
        } else {
            setMeasuredDimension(measuredWidth, measuredHeight)
        }
    }
}