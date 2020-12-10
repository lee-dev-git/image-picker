package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemSelectedImageBinding
import a.alt.z.imagepicker.model.Image
import a.alt.z.imagepicker.util.layoutInflater
import android.graphics.Bitmap
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class CropResultAdapter: ListAdapter<Bitmap, CropResultViewHolder>(callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CropResultViewHolder
            = CropResultViewHolder(ItemSelectedImageBinding.inflate(parent.layoutInflater, parent, false))

    override fun onBindViewHolder(holder: CropResultViewHolder, position: Int) = holder.bind(getItem(position))

    companion object {
        private val callback = object: DiffUtil.ItemCallback<Bitmap>() {
            override fun areItemsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean = oldItem.sameAs(newItem)
            override fun areContentsTheSame(oldItem: Bitmap, newItem: Bitmap): Boolean = oldItem.sameAs(newItem)
        }
    }
}