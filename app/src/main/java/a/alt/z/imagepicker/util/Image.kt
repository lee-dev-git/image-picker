package a.alt.z.imagepicker.util

import a.alt.z.imagepicker.model.Bucket
import a.alt.z.imagepicker.model.Image
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun loadImages(context: Context): List<Image> {

    val images = mutableListOf<Image>()

    val contentURI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projections = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    )

    val selection = MediaStore.Images.ImageColumns.SIZE + " > 0"

    val sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"

    context.contentResolver
        .query(contentURI, projections, selection, null, sortOrder)
        ?.use { cursor ->
            cursor.run {
                if(moveToFirst()) {
                    val idIndex = getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val bucketIdIndex = getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
                    val bucketNameIndex = getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

                    do {
                        val uri = ContentUris.withAppendedId(contentURI, getLong(idIndex))
                        val bucketId = getLong(bucketIdIndex)
                        val bucketName = getString(bucketNameIndex)

                        val image = Image(Bucket(bucketId, bucketName), uri)

                        images.add(image)
                    } while (moveToNext())
                }
            }
        }

    return images
}