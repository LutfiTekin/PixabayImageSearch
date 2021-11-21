package tekin.lutfi.pixabay.data


import com.google.gson.annotations.SerializedName

data class PixabayImage(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("comments")
    val comments: Int?,
    @SerializedName("downloads")
    val downloads: Int?,
    @SerializedName("largeImageURL")
    val largeImageURL: String?,
    @SerializedName("likes")
    val likes: Int?,
    @SerializedName("previewURL")
    val previewURL: String?,
    @SerializedName("tags")
    val tags: String?,
    @SerializedName("user")
    val user: String?,
    @SerializedName("userImageURL")
    val userImageURL: String?,
    @SerializedName("views")
    val views: Int?
){
    /**
     * Get three shortest tags from tags
     */
    val shortTags: String
        get() {
            val list = tags?.split(",") ?: emptyList()
            return list.sortedBy { it.length }
                .take(3)
                .joinToString(" ")
        }

    val detailTags: String
        get() {
            val list = tags?.split(",") ?: emptyList()
            return list.joinToString(" ")
        }

}