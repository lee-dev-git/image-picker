package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemAlbumBinding
import a.alt.z.imagepicker.model.Album
import android.annotation.SuppressLint
import android.net.Uri
import android.util.TypedValue
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlin.math.roundToInt

class AlbumViewHolder(
    private val binding: ItemAlbumBinding,
    private val onClickAction: (Album, List<Uri>) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album, uris: List<Uri>) {
        binding.apply {
            itemAlbumRootLayout.setOnClickListener { onClickAction(album, uris) }

            val radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F, root.context.resources.displayMetrics)
            Glide
                .with(itemAlbumImageView)
                .load(uris.first())
                .transform(
                    CenterCrop(),
                    RoundedCorners(radius.roundToInt())
                )
                .into(itemAlbumImageView)

            itemAlbumNameTextView.text = album.name

            @SuppressLint("SetTextI18n")
            itemAlbumCountTextView.text = "${uris.size}장"
        }
    }
}