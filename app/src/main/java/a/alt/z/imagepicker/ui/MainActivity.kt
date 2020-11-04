package a.alt.z.imagepicker.ui

import a.alt.z.imagepicker.databinding.ActivityMainBinding
import a.alt.z.imagepicker.model.Album
import a.alt.z.imagepicker.util.getImagesGroupByAlbum
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private val permissionLauncher = registerForActivityResult(RequestPermission()) { granted ->
        if(granted) {
            val images = getImagesGroupByAlbum(this)

            val all = Album(0L, "전체보기")
            binding.mainAlbumNameTextView.text = all.name
            images[all]?.size
                ?.let {
                    @SuppressLint("SetTextI18n")
                    binding.mainAlbumCountTextView.text = "${it}장"
                }

            albumAdapter = AlbumAdapter(images.toList(), ::onAlbumSelect)
            binding.mainAlbumRecyclerView.adapter = albumAdapter
        }
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val images = getImagesGroupByAlbum(this)
            albumAdapter = AlbumAdapter(images.toList(), ::onAlbumSelect)
            binding.mainAlbumRecyclerView.adapter = albumAdapter
        }
        else
            permissionLauncher.launch(READ_EXTERNAL_STORAGE)

        init()
    }

    private fun init() {
        binding.apply {
            mainAlbumNameTextView.setOnClickListener { showOrHideAlbumList() }
            mainAlbumCountTextView.setOnClickListener { showOrHideAlbumList() }

            mainSelectedImageRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            mainImageRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)
            mainAlbumRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun onAlbumSelect(album: Album, uris: List<Uri>) {
        binding.apply {
            mainAlbumNameTextView.text = album.name
            @SuppressLint("SetTextI18n")
            mainAlbumCountTextView.text = "${uris.size}장"
        }
    }

    private fun showOrHideAlbumList() {
        binding.mainAlbumRecyclerView.isVisible = !binding.mainAlbumRecyclerView.isVisible
    }
}