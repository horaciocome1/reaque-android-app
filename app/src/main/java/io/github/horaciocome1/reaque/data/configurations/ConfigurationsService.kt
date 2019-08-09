package io.github.horaciocome1.reaque.data.configurations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.BuildConfig
import io.github.horaciocome1.reaque.util.addSafeSnapshotListener

class ConfigurationsService : ConfigurationsInterface {

    private val versionCode: Int by lazy {
        BuildConfig.VERSION_CODE
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
            .addSafeSnapshotListener { snapshot ->
                snapshot["version_code"]?.let {
                    isUpdateAvailable.value = versionCode < it.toString().toInt()
                }
            }
        return isUpdateAvailable
    }

    override fun getLatestVersionName(): LiveData<String> {
        db.document("configurations/default")
            .addSafeSnapshotListener { snapshot ->
                snapshot["version_name"]?.let {
                    latestVersionName.value = it.toString()
                }
            }
        return latestVersionName
    }

}