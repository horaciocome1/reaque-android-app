package io.github.horaciocome1.reaque.data.configurations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.BuildConfig

class ConfigurationsService : ConfigurationsInterface {

    private val versionCode: Int by lazy {
        BuildConfig.VERSION_CODE
    }

    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val isUpdateAvailable: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
            .apply {
                value = false
            }
    }

    private val latestVersionName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
            .apply {
                value = ""
            }
    }

    override fun isUpdateAvailable(): LiveData<Boolean> {
        isUpdateAvailable.value = false
        db.document("configurations/default")
            .addSnapshotListener { snapshot, exception ->
                if (exception == null && snapshot != null && snapshot.contains("version_code"))
                    isUpdateAvailable.value = versionCode < snapshot["version_code"].toString().toInt()
            }
        return isUpdateAvailable
    }

    override fun getLatestVersionName(): LiveData<String> {
        db.document("configurations/default")
            .addSnapshotListener { snapshot, exception ->
                if (exception == null && snapshot != null && snapshot.contains("version_name"))
                    latestVersionName.value = snapshot["version_name"].toString()
            }
        return latestVersionName
    }

}