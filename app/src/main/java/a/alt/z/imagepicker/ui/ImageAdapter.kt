package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemImageBinding
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ImageAdapter: ListAdapter<Uri, ImageViewHolder>(callback) {

    private val ViewGroup.layoutInflater get() = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder
            = ImageViewHolder(ItemImageBinding.inflate(parent.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val callback = object: DiffUtil.ItemCallback<Uri>() {

            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
        }
    }
}