package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemSelectedImageBinding
import a.alt.z.imagepicker.model.Image
import android.graphics.Bitmap
import androidx.recyclerview.widget.RecyclerView

class CropResultViewHolder(
    private val binding: ItemSelectedImageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(bitmap: Bitmap) {
        binding.apply {
            itemSelectedImageRootLayout.setOnClickListener { /*onClickAction(image)*/ }
            binding.itemCropResultImageView.setImageBitmap(bitmap)
        }
    }
}