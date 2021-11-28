package tekin.lutfi.pixabay.di

import android.content.Context
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tekin.lutfi.pixabay.BuildConfig
import tekin.lutfi.pixabay.api.datasource.PixabayRepository
import tekin.lutfi.pixabay.api.service.PixabayService
import tekin.lutfi.pixabay.utils.*
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val DEFAULT_PAGE_SIZE = 20

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://pixabay.com/"
    private const val DEFAULT_TIMEOUT = 20000L

    private const val CACHE_DIR = "rf_cache"
    private const val API_KEY_INTERCEPTOR = "aki"
    private const val CACHE_INTERCEPTOR = "ci"
    private const val LOGGING_INTERCEPTOR = "li"

    @Provides
    @Singleton
    fun providesPixabayApi(service: PixabayService): PixabayRepository {
        return PixabayRepository(service)
    }

    @Provides
    @Singleton
    fun providesPixabayService(retrofit: Retrofit): PixabayService {
        return retrofit.create(PixabayService::class.java)
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().serializeNulls().create()
                )
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        @Named(API_KEY_INTERCEPTOR) apiKeyInterceptor: Interceptor,
        @Named(CACHE_INTERCEPTOR) cacheInterceptor: Interceptor,
        @Named(LOGGING_INTERCEPTOR) loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val httpCacheDirectory = File(context.cacheDir, CACHE_DIR)
        val responseCache = with(httpCacheDirectory) {
            val cacheSize: Long = 50L * 1024L * 1024L
            Cache(this, cacheSize)
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


    /**
     * Supply every request with api key query parameters
     */
    @Provides
    @Singleton
    @Named(API_KEY_INTERCEPTOR)
    fun providesApiKeyInterceptor() = Interceptor { chain ->
        var request: Request = chain.request()

        val url: HttpUrl = request.url.newBuilder()
            .addQueryParameter("key", apiKey)
            .addQueryParameter("per_page", DEFAULT_PAGE_SIZE.toString()).build()
        request = request.newBuilder().url(url).build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    @Named(CACHE_INTERCEPTOR)
    fun provideCacheInterceptor(@ApplicationContext context: Context) = Interceptor { chain ->
        var request = chain.request()
        val cacheControl =
            if (context.isInternetAvailable) CacheControl.FORCE_NETWORK else CacheControl.FORCE_CACHE
        request = request
            .newBuilder()
            .cacheControl(cacheControl)
            .build()
        chain.proceed(request)
    }

    @Provides
    @Singleton
    @Named(LOGGING_INTERCEPTOR)
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            if (BuildConfig.DEBUG) {
                level = HttpLoggingInterceptor.Level.BODY
            }
        }
    }
}