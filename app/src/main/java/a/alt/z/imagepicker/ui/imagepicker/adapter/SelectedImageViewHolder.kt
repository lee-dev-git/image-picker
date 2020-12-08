package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemSelectedImageBinding
import a.alt.z.imagepicker.model.Image
import androidx.recyclerview.widget.RecyclerView

class SelectedImageViewHolder(
    private val binding: ItemSelectedImageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(image: Image) {
        binding.apply {
            itemSelectedImageRootLayout.setOnClickListener { /*onClickAction(image)*/ }
            itemSelectedImageCropView.setUri(image.uri)
        }
    }
}