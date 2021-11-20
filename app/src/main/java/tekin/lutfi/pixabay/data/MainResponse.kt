package tekin.lutfi.pixabay.data


import com.google.gson.annotations.SerializedName

data class MainResponse(
    @SerializedName("hits")
    val images: List<PixabayImage>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalHits")
    val totalHits: Int
)