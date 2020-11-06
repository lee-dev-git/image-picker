package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemSelectedImageBinding
import a.alt.z.imagepicker.util.layoutInflater
import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class SelectedImageAdapter(
    private val onClickAction: (uri: Uri) -> Unit
): ListAdapter<Uri, SelectedImageViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder
            = SelectedImageViewHolder(ItemSelectedImageBinding.inflate(parent.layoutInflater, parent, false), onClickAction)

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val callback = object: DiffUtil.ItemCallback<Uri>() {

            override fun areItemsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: Uri, newItem: Uri): Boolean = oldItem == newItem
        }
    }
}