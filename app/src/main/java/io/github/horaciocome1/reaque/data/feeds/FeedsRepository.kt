package io.github.horaciocome1.reaque.data.feeds

class FeedsRepository private constructor(private val service: FeedsService) : FeedsServiceInterface {

    override fun get() = service.get()

    override fun requestFeed() = service.requestFeed()

    companion object {

        @Volatile
        private var instance: FeedsRepository? = null

        fun getInstance(service: FeedsService) = instance
            ?: synchronized(this) {
                instance ?: FeedsRepository(service).also { instance = it }
            }

    }

}