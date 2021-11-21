package tekin.lutfi.pixabay.utils

import android.content.Context
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tekin.lutfi.pixabay.BuildConfig
import java.io.File
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://pixabay.com/"
const val DEFAULT_TIMEOUT = 20000L
const val DEFAULT_PAGE_SIZE = 20

val defaultOkHttpClient: OkHttpClient
    get() {
        val httpCacheDirectory = Cache.dir
        val responseCache = with(httpCacheDirectory){
            val cacheSize: Long = 50L * 1024L * 1024L
            Cache(this ?: File(""), cacheSize)
        }
        val builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(cacheInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(responseCache)
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

private val cacheInterceptor = Interceptor { chain ->
    var request = chain.request()
    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
    request = request
        .newBuilder()
        .header("Cache-Control", "public, max-age=$maxStale")
        .build()
    chain.proceed(request)
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

val Context.retrofitCache: File
    get() {
        return File(cacheDir, "rf_cache")
    }


object Cache{
    var dir: File? = null
}