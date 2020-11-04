package a.alt.z.imagepicker.util

import a.alt.z.imagepicker.model.Album
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore

fun getImagesGroupByAlbum(context: Context): Map<Album, List<Uri>> {
    val images = mutableMapOf<Album, List<Uri>>()
    val total = Album(0L, "전체보기")
    images[total] = emptyList()

    val uris = mutableListOf<Uri>()

    val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projections = arrayOf(
        MediaStore.Images.ImageColumns._ID,
        MediaStore.Images.ImageColumns.BUCKET_ID,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME
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

                    do {
                        val uri = ContentUris.withAppendedId(contentUri, getLong(idIndex))
                        uris.add(uri)

                        val folderId = getLong(bucketIdIndex)
                        val folderName = getString(bucketNameIndex)
                        val album = Album(folderId, folderName)

                        images[album]
                            ?.let { uris ->
                                images[album] = uris.toMutableList()
                                    .apply { add(uri) }
                            }
                            ?: images.put(album, listOf(uri))
                    } while (moveToNext())
                }
            }
        }

    images[total] = uris

    return images
}