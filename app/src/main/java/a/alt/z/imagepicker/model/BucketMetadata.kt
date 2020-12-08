package a.alt.z.imagepicker.model

import android.net.Uri

data class BucketMetadata(
    val bucket: Bucket,
    val thumbnail: Uri,
    val size: Int
)