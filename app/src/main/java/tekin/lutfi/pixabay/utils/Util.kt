package tekin.lutfi.pixabay.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
 * https://gist.github.com/mirmilad/f7feb8007d6b572150cb84fef0b65879
 */
fun <T> LiveData<T>.debounce(duration: Long = 1000L, coroutineScope: CoroutineScope) = MediatorLiveData<T>().also { mld ->

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


