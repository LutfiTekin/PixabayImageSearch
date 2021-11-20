package tekin.lutfi.pixabay.api.datasource

import tekin.lutfi.pixabay.api.service.PixabayService
import tekin.lutfi.pixabay.data.PixabayImage
import tekin.lutfi.pixabay.utils.DEFAULT_PAGE_SIZE
import tekin.lutfi.pixabay.utils.defaultRetrofit

class PixabayApi() {
        var page = 0
        var query = "fruits"
        var color: String? = null

        private val service = defaultRetrofit.create(PixabayService::class.java)

        suspend fun getImages(requestedPage: Int): List<PixabayImage>?{
            page = requestedPage
            val images = service.getImages(query, page, color)
            return images.body()?.images
        }

}