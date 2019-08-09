package io.github.horaciocome1.reaque.data.configurations

class ConfigurationsRepository private constructor(
    private val service: ConfigurationsService
) : ConfigurationsInterface {

    override fun isUpdateAvailable() = service.isUpdateAvailable()

    override fun getLatestVersionName() = service.getLatestVersionName()

    companion object {

        @Volatile
        private var instance: ConfigurationsRepository? = null

        fun getInstance(service: ConfigurationsService) = instance ?: synchronized(this) {
            instance ?: ConfigurationsRepository(service)
                .also {
                    instance = it
                }
        }

    }

}