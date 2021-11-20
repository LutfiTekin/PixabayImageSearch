package tekin.lutfi.pixabay.api.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import tekin.lutfi.pixabay.data.MainResponse


interface PixabayService {


    @GET("api")
    suspend fun getImages(@Query("q") query: String,
                  @Query("page") page: Int): Response<MainResponse>

}