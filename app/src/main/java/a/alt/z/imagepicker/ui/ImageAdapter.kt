package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemImageBinding
import a.alt.z.imagepicker.util.layoutInflater
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ImageAdapter(
    private val onClickAction: (uri: Uri) -> Unit
): ListAdapter<Uri, ImageViewHolder>(callback) {

    var selectedImages = listOf<Uri>()
        set(value) {
            val old = field
            field = value
            if(old.size > field.size) {
                old.forEach {
                    val position = currentList.indexOf(it)
                    if(position >= 0) notifyItemChanged(currentList.indexOf(it))
                }
            } else {
                field.forEach {
                    val position = currentList.indexOf(it)
                    if(position >= 0) notifyItemChanged(currentList.indexOf(it))
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder
            = ImageViewHolder(ItemImageBinding.inflate(parent.layoutInflater, parent, false), onClickAction)

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = getItem(position)
        holder.bind(uri, selectedImages.contains(uri), selectedImages.indexOf(uri))
    }

    companion object {
        private val callback = object: DiffUtil.ItemCallback<Uri>() {

            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
        }
    }
}