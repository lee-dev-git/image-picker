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

class ImagePickerViewModel @ViewModelInject constructor(): ViewModel() {

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

                if(size >= 10 && !contains)  {
                    /* 10장 제한 */
                } else {
                    if(contains) { /* 포커싱 또는 삭제 */
                        if(hasFocus) { /* 삭제 */
                            selectedImages.toMutableList()
                                    .apply { remove(image) }
                                    .let {
                                        _selectedImages.value = it
                                        _focusedImage.value = it.lastOrNull()
                                    }
                        } else { /* 포커싱 */
                            _focusedImage.value = image
                        }
                    } else {
                        /* 추가 */
                        selectedImages.toMutableList()
                                .apply { add(image) }
                                .let {
                                    _selectedImages.value = it
                                    _focusedImage.value = image
                                }

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