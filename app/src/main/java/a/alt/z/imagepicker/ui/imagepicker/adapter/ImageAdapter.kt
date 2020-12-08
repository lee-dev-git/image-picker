package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemImageBinding
import a.alt.z.imagepicker.model.Image
import a.alt.z.imagepicker.util.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ImageAdapter(
    private val onClickAction: (Image) -> Unit
): ListAdapter<Image, ImageViewHolder>(callback) {

    var focusedImage: Image? = null
        set(value) {
            val old = field
            field = value

            currentList.indexOf(old)
                .takeIf { it >= 0 }
                ?.let { position -> notifyItemChanged(position) }

            currentList.indexOf(field)
                .takeIf { it >= 0 }
                ?.let { position -> notifyItemChanged(position) }
        }

    var selectedImages = listOf<Image>()
        set(value) {
            val old = field
            field = value
            if(old.size > field.size) {
                old.forEach {
                    currentList.indexOf(it)
                        .takeIf { position -> position >= 0 }
                        ?.let { position -> notifyItemChanged(position) }
                }
            } else {
                val added = field.last()
                currentList.indexOf(added)
                    .takeIf { it >= 0 }
                    ?.let { position -> notifyItemChanged(position) }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder
            = ImageViewHolder(ItemImageBinding.inflate(parent.layoutInflater, parent, false), onClickAction)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = getItem(position)
        val hasFocus = focusedImage == image
        val index = selectedImages.indexOf(image)
        holder.bind(image, hasFocus, index)
    }

    companion object {
        private val callback = object: DiffUtil.ItemCallback<Image>() {

            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.bucket == newItem.bucket
                        && oldItem.uri == newItem.uri
            }
        }
    }
}