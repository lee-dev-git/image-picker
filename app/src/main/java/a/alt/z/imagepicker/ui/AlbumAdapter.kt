package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ItemAlbumBinding
import a.alt.z.imagepicker.model.Album
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AlbumAdapter(
    private val images: List<Pair<Album, List<Uri>>>,
    private val onClickAction: (Album, List<Uri>) -> Unit
): RecyclerView.Adapter<AlbumViewHolder>() {

    private val ViewGroup.layoutInflater get() = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder
            = AlbumViewHolder(ItemAlbumBinding.inflate(parent.layoutInflater, parent, false), onClickAction)

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        images[position].let { pair ->
            val album = pair.first
            val uris = pair.second
            holder.bind(album, uris)
        }
    }

    override fun getItemCount(): Int = images.size
}