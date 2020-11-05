package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemImageBinding
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ImageViewHolder(
    private val binding: ItemImageBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(uri: Uri) {
        binding.apply {
            Glide.with(itemImageImageView)
                .load(uri)
                .centerCrop()
                .into(itemImageImageView)
        }
    }
}