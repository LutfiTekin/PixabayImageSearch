package tekin.lutfi.pixabay.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import tekin.lutfi.pixabay.R
import tekin.lutfi.pixabay.databinding.ActivityLandingBinding

@AndroidEntryPoint
class Landing : AppCompatActivity() {

    private var binding: ActivityLandingBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        fetchRemoteConfigAndRedirect()
    }


    /**
     * Fetch api from firebase remote config
     */
    private fun fetchRemoteConfigAndRedirect() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config)
            fetchAndActivate().addOnCompleteListener {
                startActivity(Intent(this@Landing, MainActivity::class.java))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}