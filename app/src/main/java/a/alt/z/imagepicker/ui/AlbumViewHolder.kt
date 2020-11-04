package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemAlbumBinding
import a.alt.z.imagepicker.model.Album
import android.annotation.SuppressLint
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AlbumViewHolder(
    private val binding: ItemAlbumBinding,
    private val onClickAction: (Album, List<Uri>) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(album: Album, uris: List<Uri>) {
        binding.apply {
            itemAlbumRootLayout.setOnClickListener { onClickAction(album, uris) }
            Glide
                .with(itemAlbumImageView)
                .load(uris.first())
                .centerCrop()
                .into(itemAlbumImageView)
            itemAlbumNameTextView.text = album.name
            @SuppressLint("SetTextI18n")
            itemAlbumCountTextView.text = "${uris.size}장"
        }
    }
}