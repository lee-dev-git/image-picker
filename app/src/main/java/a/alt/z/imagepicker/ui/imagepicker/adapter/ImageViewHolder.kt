package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemImageBinding
import a.alt.z.imagepicker.model.Image
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageViewHolder(
    private val binding: ItemImageBinding,
    private val onClickAction: (Image) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image, hasFocus: Boolean, index: Int) {
        binding.apply {
            itemImageRootLayout.setOnClickListener { onClickAction(image) }

            Glide.with(itemImageImageView)
                .load(image.uri)
                .centerCrop()
                .into(itemImageImageView)

            binding.itemImageMask.isVisible = hasFocus

            itemImageIndexTextView.isVisible = index >= 0
            if(index >= 0) itemImageIndexTextView.text = (index + 1).toString()
        }
    }
}