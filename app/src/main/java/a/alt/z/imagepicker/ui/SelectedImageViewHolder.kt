package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemSelectedImageBinding
import android.net.Uri
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlin.math.roundToInt

class SelectedImageViewHolder(
    private val binding: ItemSelectedImageBinding,
    private val onClickAction: (uri: Uri) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: Uri) {
        binding.apply {
            itemSelectedImageRootLayout.setOnClickListener { onClickAction(uri) }

            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, root.context.resources.displayMetrics)
            Glide.with(itemSelectedImageImageView)
                .load(uri)
                .transform(
                    CenterCrop(),
                    RoundedCorners(radius.roundToInt())
                )
                .into(itemSelectedImageImageView)
        }
    }
}