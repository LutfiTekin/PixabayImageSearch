package tekin.lutfi.pixabay.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

val apiKey: String
    get() {
        return Firebase.remoteConfig.getString("api_key")
    }


