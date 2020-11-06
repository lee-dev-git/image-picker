package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemImageBinding
import android.net.Uri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageViewHolder(
    private val binding: ItemImageBinding,
    private val onClickAction: (uri: Uri) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: Uri, isSelected: Boolean, index: Int) {
        binding.apply {
            itemImageRootLayout.setOnClickListener { onClickAction(uri) }

            Glide.with(itemImageImageView)
                .load(uri)
                .centerCrop()
                .into(itemImageImageView)

            itemImageMask.isVisible = isSelected
            itemImageIndexTextView.isVisible = isSelected
            if(index >= 0) itemImageIndexTextView.text = (index + 1).toString()
        }
    }
}