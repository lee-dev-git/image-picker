package a.alt.z.imagepicker.ui.imagepicker.adapter

import a.alt.z.imagepicker.databinding.ItemBucketBinding
import a.alt.z.imagepicker.model.BucketMetadata
import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlin.math.roundToInt

class BucketViewHolder(
    private val binding: ItemBucketBinding,
    private val onClickAction: (BucketMetadata) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(metadata: BucketMetadata) {
        binding.apply {
            itemBucketRootLayout.setOnClickListener { onClickAction(metadata) }

            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, root.context.resources.displayMetrics)
            Glide
                .with(itemBucketImageView)
                .load(metadata.thumbnail)
                .transform(
                    CenterCrop(),
                    RoundedCorners(radius.roundToInt())
                )
                .into(itemBucketImageView)

            itemBucketNameTextView.text = metadata.bucket.name
            @SuppressLint("SetTextI18n")
            itemBucketCountTextView.text = "${metadata.size}장"
        }
    }
}