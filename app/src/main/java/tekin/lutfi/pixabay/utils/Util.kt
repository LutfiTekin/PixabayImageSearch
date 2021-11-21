package tekin.lutfi.pixabay.utils

import android.app.AlertDialog
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.*
import tekin.lutfi.pixabay.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.os.Build


val apiKey: String
    get() {
        return Firebase.remoteConfig.getString("api_key")
    }


val acceptedColors = hashMapOf(
    "red" to "#FF0000",
    "orange" to "#FFA500",
    "yellow" to "#FFFF00",
    "green" to "#00FF00",
    "turquoise" to "#30D5C8",
    "blue" to "#0000FF",
    "lilac" to "#C8A2C8",
    "white" to "#FFFFFF",
    "gray" to "#808080",
    "black" to "#000000",
    "brown" to "#964B00",
)


/**
 * Borrowed from https://gist.github.com/mirmilad/f7feb8007d6b572150cb84fef0b65879
 */
fun <T> LiveData<T>.debounce(duration: Long = 1000L, coroutineScope: CoroutineScope) =
    MediatorLiveData<T>().also { mld ->

        val source = this
        var job: Job? = null

        mld.addSource(source) {
            job?.cancel()
            job = coroutineScope.launch {
                delay(duration)
                mld.value = source.value
            }
        }
    }

/**
 * Borrowed from https://github.com/Kotlin/kotlinx.coroutines/issues/1874#issuecomment-936951673
 * Check if the cancellable coroutine is active before resuming
 */
fun <T> CancellableContinuation<T>.resumeSafely(value: T) {
    if (isActive) {
        resume(value)
    }
}


suspend fun confirm(
    context: Context,
    @StringRes title: Int,
    @StringRes content: Int,
    @StringRes negativeButton: Int = R.string.no,
    @StringRes positiveButton: Int = R.string.yes
): Boolean {
    return suspendCancellableCoroutine { c ->
        val dialog = AlertDialog.Builder(context)
        dialog.setCancelable(true)
            .setTitle(title)
            .setMessage(content)
            .setNegativeButton(negativeButton) { dialogInterface, _ ->
                dialogInterface.dismiss()
                c.resumeSafely(false)
            }
            .setPositiveButton(positiveButton) { dialogInterface, _ ->
                dialogInterface.dismiss()
                c.resumeSafely(true)
            }
        dialog.show()
    }
}

val isInternetAvailable: Boolean
    get() {
        val context: Context = App.instance.applicationContext
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(TRANSPORT_WIFI)
                    || actNw.hasTransport(TRANSPORT_CELLULAR)
                    || actNw.hasTransport(TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


