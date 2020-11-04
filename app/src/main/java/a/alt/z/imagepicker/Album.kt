package a.alt.z.imagepicker

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

data class Album(
    val id: String,
    val name: String,
    val images: List<Image>
)

data class Image(
    val uri: Uri,
    val folderId: Long,
    val folderName: String,
    val dateAdded: Long
)

fun getAlbumList(context: Context) {
    val images = mutableListOf<Image>()

    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projections = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Images.ImageColumns.DATE_ADDED
    )
    val selection = MediaStore.Images.ImageColumns.SIZE + " > 0"
    val sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC"

    context.contentResolver
        .query(contentUri, projections, selection, null, sortOrder)
        ?.use { cursor ->
            cursor.run {
                if(moveToFirst()) {
                    val idIndex = getColumnIndexOrThrow(MediaStore.Images.ImageColumns._ID)
                    val bucketIdIndex = getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID)
                    val bucketNameIndex = getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)
                    val dateAddedIndex = getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_ADDED)

                    do {
                        val uri = ContentUris.withAppendedId(contentUri, getLong(idIndex))
                        val folderId = getLong(bucketIdIndex)
                        val folderName = getString(bucketNameIndex)
                        val dateAdded = getLong(dateAddedIndex)

                        val image = Image(uri, folderId, folderName, dateAdded)

                        images.add(image)
                    } while (moveToNext())
                }
            }
        }

    if(images.isNotEmpty()) {
        val group = images.groupBy { it.folderId }
    }
}