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
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val permissionLauncher = registerForActivityResult(RequestPermission()) { granted ->
        if(granted) {
            val images = getImagesGroupByAlbum(this)
            dataBinding(images)
        }
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var albumAdapter: AlbumAdapter

    private val imageAdapter = ImageAdapter(::onImageSelect)

    private val selectedImageAdapter = SelectedImageAdapter(::onSelectedImageClick)

    private var selectedImages: List<Uri> by Delegates.observable(emptyList()) { _, _, new ->
        binding.mainSelectedImageRecyclerView.isVisible = new.isNotEmpty()
        selectedImageAdapter.submitList(new)
        imageAdapter.selectedImages = new
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            val images = getImagesGroupByAlbum(this)
            dataBinding(images)
        }
        else
            permissionLauncher.launch(READ_EXTERNAL_STORAGE)

        init()
    }

    private fun init() {
        binding.apply {
            mainAlbumNameTextView.setOnClickListener { showOrHideAlbumList() }
            mainAlbumCountTextView.setOnClickListener { showOrHideAlbumList() }

            mainSelectedImageRecyclerView.adapter = selectedImageAdapter
            mainSelectedImageRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)

            mainImageRecyclerView.adapter = imageAdapter
            mainImageRecyclerView.layoutManager = GridLayoutManager(this@MainActivity, 3)

            mainAlbumLayout.setOnClickListener { mainAlbumLayout.isVisible = false }
            mainAlbumRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun dataBinding(images: Map<Album, List<Uri>>) {
        val all = Album(0L, "전체보기")
        val uris = images[all]

        binding.mainAlbumNameTextView.text = all.name
        @SuppressLint("SetTextI18n")
        binding.mainAlbumCountTextView.text = "${uris?.size ?: 0}장"
        imageAdapter.submitList(uris)

        albumAdapter = AlbumAdapter(images.toList(), ::onAlbumSelect)
        binding.mainAlbumRecyclerView.adapter = albumAdapter
    }

    private fun onAlbumSelect(album: Album, uris: List<Uri>) {
        binding.apply {
            mainAlbumNameTextView.text = album.name
            @SuppressLint("SetTextI18n")
            mainAlbumCountTextView.text = "${uris.size}장"

            imageAdapter.submitList(uris)

            binding.mainAlbumLayout.isVisible = false
        }
    }

    private fun showOrHideAlbumList() {
        binding.mainAlbumLayout.isVisible = !binding.mainAlbumLayout.isVisible
    }

    private fun onImageSelect(uri: Uri) {
        selectedImages = selectedImages.toMutableList().apply {
            if(selectedImages.contains(uri)) remove(uri)
            else add(uri)
        }
    }

    private fun onSelectedImageClick(uri: Uri) {
        selectedImages = selectedImages.toMutableList().apply { remove(uri) }
    }
}