package tekin.lutfi.pixabay.api.datasource

import tekin.lutfi.pixabay.api.service.PixabayService
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.utils.Resource


class PixabayRepository(private val service: PixabayService) {
        var page = 0
        var query = "fruits"
        var color: String? = null

        suspend fun getImages(requestedPage: Int): Resource<List<PixabayImage>?>{
            page = requestedPage
            val images = service.getImages(query, page, color)
            return if (images.isSuccessful)
                Resource.Success(images.body()?.images)
            else Resource.Error(images.message())
        }

}