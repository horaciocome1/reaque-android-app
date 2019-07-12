package io.github.horaciocome1.reaque.data.feed

class FeedRepository private constructor(private val service: FeedService) : FeedInterface {

    override fun get() = service.get()

    companion object {

        @Volatile
        private var instance: FeedRepository? = null

        fun getInstance(service: FeedService) = instance
            ?: synchronized(this) {
                instance ?: FeedRepository(service).also { instance = it }
            }

    }

}