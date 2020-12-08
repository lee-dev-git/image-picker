package a.alt.z.imagepicker.model

data class Bucket(
    val id: Long,
    val name: String
)

val BUCKET_ALL_IMAGES = Bucket(0L, "전체보기")