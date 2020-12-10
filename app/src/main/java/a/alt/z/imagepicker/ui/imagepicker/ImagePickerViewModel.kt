package a.alt.z.imagepicker.ui.imagepicker

import a.alt.z.imagepicker.model.BUCKET_ALL_IMAGES
import a.alt.z.imagepicker.model.Bucket
import a.alt.z.imagepicker.model.BucketMetadata
import a.alt.z.imagepicker.model.Image
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

/**
 * focused
 * selected
 *
 * 1. 사용자가 이미지를 선택
 *
 * 2.1 이미지 추가
 * 2.2 이미지 삭제
 * 2.3 다른 이미지 수정
 *
 * 3.1 selected images + new image
 *     focused image = new image
 *     기존 focused image 가 있다면 데이터 저장
 *
 * 3.2 selected images - image
 *     focused image = selected images 의 마지막 image
 *     focused image data - image data
 *
 * 3.3 focused image = selected image
 *     기존 focused image 데이터 저장
 */
class ImagePickerViewModel @ViewModelInject constructor(): ViewModel() {

    private val _ratio = MutableLiveData(Ratio.RATIO_1_1)
    val ratio: LiveData<Ratio> = _ratio

    fun changeRatio() {
        when(_ratio.value) {
            Ratio.RATIO_1_1 -> _ratio.postValue(Ratio.RATIO_4_3)
            Ratio.RATIO_4_3 -> _ratio.postValue(Ratio.RATIO_1_1)
        }
    }

    private val _images = MutableLiveData<List<Image>>()
    val images: LiveData<List<Image>> = _images

    fun onLoadFinished(images: List<Image>) = _images.postValue(images)

    val bucketMetadata: LiveData<List<BucketMetadata>> = _images.map { images ->
        val all = BucketMetadata(
            BUCKET_ALL_IMAGES,
            images.firstOrNull()?.uri ?: Uri.EMPTY,
            images.size
        )

        images
            .takeIf { it.isNotEmpty() }
            ?.groupBy { it.bucket }
            ?.map {
                val bucket = it.key
                val bucketImages = it.value
                BucketMetadata(bucket, bucketImages.first().uri, bucketImages.size)
            }
            ?.let { listOf(all) + it }
            ?: listOf(all)
    }

    private val _selectedBucket = MutableLiveData<Bucket>()
    val selectedBucket: LiveData<Bucket> = _selectedBucket

    fun onBucketSelect(bucket: Bucket) = _selectedBucket.postValue(bucket)

    val filteredImages = _selectedBucket.map { selectedBucket ->
        if(selectedBucket == BUCKET_ALL_IMAGES)
            _images.value
                ?: emptyList()
        else
            _images.value
                ?.takeIf { it.isNotEmpty() }
                ?.filter { it.bucket == selectedBucket }
                ?: emptyList()
    }

    private val _selectedImages = MutableLiveData<List<Image>>()
    val selectedImages: LiveData<List<Image>> = _selectedImages

    fun onImageSelect(image: Image) {
        _selectedImages
            .value
            ?.let { selectedImages ->
                val contains = selectedImages.contains(image)
                val hasFocus = focusedImage.value == image
                val size = selectedImages.size

                if(!contains) {
                    if(size >= 10) {

                    } else {
                        selectedImages.toMutableList()
                                .apply { add(image) }
                                .let {
                                    _selectedImages.value = it
                                    _focusedImage.value = image
                                }
                    }
                } else {
                    if(hasFocus) {
                        selectedImages.toMutableList()
                                .apply { remove(image) }
                                .let {
                                    _selectedImages.value = it
                                    _focusedImage.value = it.lastOrNull()
                                }
                    } else {
                        _focusedImage.value = image
                    }
                }
            }
            ?: Unit.apply {
                _selectedImages.value = listOf(image)
                _focusedImage.value = image
            }
    }

    private val _focusedImage = MutableLiveData<Image?>()
    val focusedImage: LiveData<Image?> = _focusedImage
}

enum class Ratio {
    RATIO_1_1, RATIO_4_3
}