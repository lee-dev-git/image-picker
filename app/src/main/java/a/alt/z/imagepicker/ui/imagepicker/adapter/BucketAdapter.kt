package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemBucketBinding
import a.alt.z.imagepicker.model.BucketMetadata
import a.alt.z.imagepicker.util.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class BucketAdapter(
    private val onClickAction: (BucketMetadata) -> Unit
): ListAdapter<BucketMetadata, BucketViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BucketViewHolder
            = BucketViewHolder(ItemBucketBinding.inflate(parent.layoutInflater, parent, false), onClickAction)

    override fun onBindViewHolder(holder: BucketViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val callback = object: DiffUtil.ItemCallback<BucketMetadata>() {

            override fun areItemsTheSame(oldItem: BucketMetadata, newItem: BucketMetadata): Boolean = oldItem == newItem

            override fun areContentsTheSame(oldItem: BucketMetadata, newItem: BucketMetadata): Boolean {
                return oldItem.bucket == newItem.bucket
                        && oldItem.size == newItem.size
                        && oldItem.thumbnail == newItem.thumbnail
            }
        }
    }
}