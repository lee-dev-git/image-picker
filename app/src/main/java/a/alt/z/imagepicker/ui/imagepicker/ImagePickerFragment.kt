package a.alt.z.imagepicker.ui.imagepicker

import a.alt.z.imagepicker.R
import a.alt.z.imagepicker.databinding.FragmentImagePickerBinding
import a.alt.z.imagepicker.model.BUCKET_ALL_IMAGES
import a.alt.z.imagepicker.model.BucketMetadata
import a.alt.z.imagepicker.model.Image
import a.alt.z.imagepicker.ui.imagepicker.adapter.BucketAdapter
import a.alt.z.imagepicker.ui.imagepicker.adapter.ImageAdapter
import a.alt.z.imagepicker.ui.imagepicker.adapter.SelectedImageAdapter
import a.alt.z.imagepicker.util.loadImages
import a.alt.z.imagepicker.util.viewBinding
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import timber.log.debug

class ImagePickerFragment: Fragment(R.layout.fragment_image_picker) {

    private val binding by viewBinding(FragmentImagePickerBinding::bind)

    private val viewModel: ImagePickerViewModel by viewModels()

    private val bucketAdapter = BucketAdapter(::onBucketSelect)

    private val imageAdapter = ImageAdapter(::onImageSelect)

    private val selectedImageAdapter = SelectedImageAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val images = withContext(Dispatchers.IO) { loadImages(requireContext()) }
            viewModel.onLoadFinished(images)
            viewModel.onBucketSelect(BUCKET_ALL_IMAGES)
        }

        init()
        setupObserver()
    }

    private fun init() {
        binding.imagePickerBack.setOnClickListener {  }
        binding.imagePickerBucketName.setOnClickListener { showOrHideBucketList() }
        binding.imagePickerUpload.setOnClickListener {  }

        binding.imagePickerSelectedImagesViewPager.adapter = selectedImageAdapter
        binding.imagePickerSelectedImagesViewPager.isUserInputEnabled = false
        binding.imagePickerSelectedImagesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        selectedImageAdapter.registerAdapterDataObserver(object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                viewModel.focusedImage
                    .value
                    ?.let { focusedImage ->
                        selectedImageAdapter.currentList
                            .indexOf(focusedImage)
                            .takeIf { it >= 0 }
                            ?.let { binding.imagePickerSelectedImagesViewPager.setCurrentItem(it, false) }
                    }
            }
        })

        binding.imagePickerRecyclerView.adapter = imageAdapter
        binding.imagePickerRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.imagePickerRecyclerView.setHasFixedSize(true)

        binding.imagePickerBucketRecyclerView.adapter = bucketAdapter
        binding.imagePickerBucketRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

    private fun onImageSelect(image: Image) = viewModel.onImageSelect(image)

    private fun onBucketSelect(metadata: BucketMetadata) = viewModel.onBucketSelect(metadata.bucket)

    private fun showOrHideBucketList() { binding.imagePickerBucketRecyclerView.apply { isVisible = !isVisible } }

    private fun setupObserver() {
        viewModel.images.observe(viewLifecycleOwner) { images ->
            imageAdapter.submitList(images)
            imageAdapter.focusedImage = images.firstOrNull()
            selectedImageAdapter.submitList(listOf(images.firstOrNull()))
        }

        viewModel.bucketMetadata.observe(viewLifecycleOwner) { bucketMetadata -> bucketAdapter.submitList(bucketMetadata) }

        viewModel.filteredImages.observe(viewLifecycleOwner) { filteredImages -> imageAdapter.submitList(filteredImages) }

        viewModel.selectedBucket.observe(viewLifecycleOwner) { selectedBucket -> binding.imagePickerBucketName.text = selectedBucket.name }

        viewModel.selectedImages.observe(viewLifecycleOwner) { selectedImages ->
            binding.imagePickerSelectedImagesViewPager.isVisible = selectedImages.isNotEmpty()
            selectedImageAdapter.submitList(selectedImages)

            imageAdapter.selectedImages = selectedImages
        }

        viewModel.focusedImage.observe(viewLifecycleOwner) { focusedImage ->
            imageAdapter.focusedImage = focusedImage
            viewModel.selectedImages.value
                    ?.indexOf(focusedImage)
                    ?.takeIf { it >= 0 }
                    ?.let { binding.imagePickerSelectedImagesViewPager.setCurrentItem(it, false) }
        }
    }
}