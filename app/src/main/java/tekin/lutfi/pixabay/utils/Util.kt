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


