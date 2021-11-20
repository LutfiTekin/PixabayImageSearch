package tekin.lutfi.pixabay.utils

import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tekin.lutfi.pixabay.BuildConfig
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://pixabay.com/"
const val DEFAULT_TIMEOUT = 20000L
const val DEFAULT_PAGE_SIZE = 20

val defaultOkHttpClient: OkHttpClient
    get() {
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
        return builder
            .build()
    }

val defaultRetrofit: Retrofit
    get() {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(defaultOkHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
    }

val loggingInterceptor: HttpLoggingInterceptor
    get() {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        }
    }

/**
 * Supply every request with api key query parameters
 */
private val apiKeyInterceptor = object : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()

        val url: HttpUrl = request.url.newBuilder()
            .addQueryParameter("key", apiKey)
            .addQueryParameter("per_page", DEFAULT_PAGE_SIZE.toString()).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}