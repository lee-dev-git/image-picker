package a.alt.z.imagepicker.ui.imagepicker

import a.alt.z.imagepicker.R
import a.alt.z.imagepicker.databinding.FragmentImagePickerBinding
import a.alt.z.imagepicker.model.BUCKET_ALL_IMAGES
import a.alt.z.imagepicker.model.BucketMetadata
import a.alt.z.imagepicker.model.Image
import a.alt.z.imagepicker.ui.imagepicker.adapter.BucketAdapter
import a.alt.z.imagepicker.ui.imagepicker.adapter.ImageAdapter
import a.alt.z.imagepicker.ui.imagepicker.adapter.CropResultAdapter
import a.alt.z.imagepicker.util.*
import a.alt.z.imagepicker.widget.CropRect
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * uri
 * bitmap
 * scale factor / crop factor
 *
 */
class ImagePickerFragment: Fragment(R.layout.fragment_image_picker) {

    private val binding by viewBinding(FragmentImagePickerBinding::bind)

    private val viewModel: ImagePickerViewModel by viewModels()

    private val bucketAdapter = BucketAdapter(::onBucketSelect)

    private val imageAdapter = ImageAdapter(::onImageSelect)

    private val cropResultAdapter = CropResultAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val images = withContext(Dispatchers.IO) { loadImages(requireContext()) }

            viewModel.onLoadFinished(images)
            viewModel.onBucketSelect(BUCKET_ALL_IMAGES)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setupObserver()
    }

    private fun init() {
        binding.imagePickerSquareRatio.setOnClickListener { viewModel.changeRatio() }
        binding.imagePickerClassicRatio.setOnClickListener { viewModel.changeRatio() }

        binding.imagePickerBack.setOnClickListener {  }
        binding.imagePickerBucketName.setOnClickListener { showOrHideBucketList() }
        binding.imagePickerUpload.setOnClickListener {
            try {
                val focusedImage = requireNotNull(viewModel.focusedImage.value)

                cropParams.entries.forEach {
                    val image = it.key
                    val cropParam = it.value

                    requireContext().contentResolver
                            .openInputStream(image.uri)
                            ?.use { inputStream ->
                                val src = BitmapFactory.decodeStream(inputStream)
                                val bitmap = Bitmap.createScaledBitmap(src, cropParam.scaleRect.width(), cropParam.scaleRect.height(), false)
                                val result = Bitmap.createBitmap(bitmap, cropParam.cropRect.left, cropParam.cropRect.top, cropParam.cropRect.width(), cropParam.cropRect.height())
                                cropResult.add(result)
                                Log.d("image-picker", "crop success")
                            }
                }
                Log.d("image-picker", "crop result size: ${cropResult.size}")
                binding.imagePickerCropResultLayout.isVisible = true
                cropResultAdapter.submitList(cropResult)
                /*
                val result = binding.imagePickerCropLayout.crop()
                binding.imagePickerCropLayout.saveCropParam()
                result?.let {
                    binding.imagePickerCropResultLayout.isVisible = true
                    Glide.with(requireContext())
                            .load(it.result)
                            .into(binding.imagePickerCropResultImageView)
                }
                */
            } catch (illegalArgumentException: IllegalArgumentException) {
                /* empty */
                Toast.makeText(requireContext(), "오늘의 사진을 선택해주세요 :)", Toast.LENGTH_SHORT).show()
            } catch (exception: Exception) {

            }
        }

        binding.imagePickerRecyclerView.adapter = imageAdapter
        binding.imagePickerRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.imagePickerRecyclerView.setHasFixedSize(true)

        binding.imagePickerBucketRecyclerView.adapter = bucketAdapter
        binding.imagePickerBucketRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.imagePickerCropResultViewPager.adapter = cropResultAdapter
        binding.imagePickerCropResultViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        lifecycleScope.launch {
            delay(500L)
            startTransition()
        }
    }

    private fun startTransition() {
        val deviceHeight = requireContext().resources.displayMetrics.heightPixels
        val deviceWidth = requireContext().resources.displayMetrics.widthPixels

        val verticalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80F, requireContext().resources.displayMetrics)
        val horizontalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56F, requireContext().resources.displayMetrics)

        val viewWidth = deviceWidth - horizontalMargin
        val viewHeight = viewWidth /* ratio 1:1 */

        val guidePercent = (verticalMargin + viewHeight) / deviceHeight
        Log.d("image-picker", "device: $deviceWidth, $deviceHeight, margin: $horizontalMargin, $verticalMargin, view: $viewWidth, $viewHeight, guidePercent: $guidePercent")
        binding.imagePickerRootLayout.updateTransition(duration = 500L) {
            setGuidelinePercent(R.id.image_picker_top_guideline, guidePercent)
            setGuidelinePercent(R.id.image_picker_bottom_guideline, guidePercent + 1F)
        }

        val cropLayoutTopGuidePercent = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64F, requireContext().resources.displayMetrics) / deviceHeight
        val cropLayoutBottomGuidePercent = cropLayoutTopGuidePercent + viewHeight / deviceHeight

        binding.imagePickerRootLayout.update {
            setGuidelinePercent(R.id.crop_layout_top_guideline, cropLayoutTopGuidePercent)
            setGuidelinePercent(R.id.crop_layout_bottom_guideline, cropLayoutBottomGuidePercent)
        }
    }

    private fun onImageSelect(image: Image) {
        viewModel.focusedImage
                .value
                ?.let { focusedImage ->
                    if(focusedImage == image) cropParams.remove(image)
                    else {
                        /*
                        binding.imagePickerCropLayout
                                .getCropParam()
                                ?.let { cropParams.put(focusedImage, it) }
                                */
                    }
                }
        viewModel.onImageSelect(image)
    }

    private fun onBucketSelect(metadata: BucketMetadata) = viewModel.onBucketSelect(metadata.bucket)

    private fun showOrHideBucketList() { binding.imagePickerBucketRecyclerView.apply { isVisible = !isVisible } }

    private var cropParams = mutableMapOf<Image, CropRect>()

    private val cropResult = mutableListOf<Bitmap>()

    private fun setupObserver() {
        viewModel.ratio.observe(viewLifecycleOwner) { ratio ->
            when(ratio) {
                Ratio.RATIO_1_1 -> {
                 //   binding.imagePickerCropLayout.onRatioChange("1:1")
                    binding.apply {
                        imagePickerSquareRatio.isVisible = true
                        imagePickerClassicRatio.isVisible = false

                        imagePickerRootLayout.updateTransition {
                            val diff = (cropLayoutBottomGuideline.guidePercent - cropLayoutTopGuideline.guidePercent) / 6
                            Log.d("image-picker", "top: ${cropLayoutTopGuideline.guidePercent}, bottom: ${cropLayoutBottomGuideline.guidePercent}, diff: $diff")
                            setGuidelinePercent(R.id.crop_layout_top_guideline, cropLayoutTopGuideline.guidePercent - diff)
                            setGuidelinePercent(R.id.crop_layout_bottom_guideline, cropLayoutBottomGuideline.guidePercent + diff)
                        }
                    }
                }
                Ratio.RATIO_4_3 -> {
                    binding.apply {
                        imagePickerSquareRatio.isVisible = false
                        imagePickerClassicRatio.isVisible = true

                        imagePickerRootLayout.updateTransition {
                            val diff = (cropLayoutBottomGuideline.guidePercent - cropLayoutTopGuideline.guidePercent) / 8
                            Log.d("image-picker", "top: ${cropLayoutTopGuideline.guidePercent}, bottom: ${cropLayoutBottomGuideline.guidePercent}, diff: $diff")
                            setGuidelinePercent(R.id.crop_layout_top_guideline, cropLayoutTopGuideline.guidePercent + diff)
                            setGuidelinePercent(R.id.crop_layout_bottom_guideline, cropLayoutBottomGuideline.guidePercent - diff)
                        }
                    }
                }
                else -> {}
            }
        }

        viewModel.images.observe(viewLifecycleOwner) { images -> imageAdapter.submitList(images) }

        viewModel.bucketMetadata.observe(viewLifecycleOwner) { bucketMetadata -> bucketAdapter.submitList(bucketMetadata) }

        viewModel.selectedBucket.observe(viewLifecycleOwner) { selectedBucket -> binding.imagePickerBucketName.text = selectedBucket.name }

        viewModel.filteredImages.observe(viewLifecycleOwner) { filteredImages -> imageAdapter.submitList(filteredImages) }

        viewModel.selectedImages.observe(viewLifecycleOwner) { selectedImages -> imageAdapter.selectedImages = selectedImages }

        viewModel.focusedImage.observe(viewLifecycleOwner) { focusedImage ->
            imageAdapter.focusedImage = focusedImage
            focusedImage
                    ?.let { binding.imagePickerCropLayout.setImageURI(it.uri) }
                    /*?: place holder */
        }
    }
}