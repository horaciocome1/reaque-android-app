package io.github.horaciocome1.reaque.data.configurations

import androidx.lifecycle.LiveData

interface ConfigurationsInterface {

    fun isUpdateAvailable(): LiveData<Boolean>

    fun getLatestVersionName(): LiveData<String>

}