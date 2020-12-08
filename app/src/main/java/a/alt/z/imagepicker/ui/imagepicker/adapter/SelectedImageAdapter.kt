package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemSelectedImageBinding
import a.alt.z.imagepicker.model.Image
import a.alt.z.imagepicker.util.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SelectedImageAdapter: ListAdapter<Image, SelectedImageViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder
            = SelectedImageViewHolder(ItemSelectedImageBinding.inflate(parent.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) = holder.bind(getItem(position))

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