package io.github.horaciocome1.reaque.data.ratings

import io.github.horaciocome1.reaque.data.posts.Post

class RatingsRepository private constructor(private val service: RatingsService) : RatingsServiceInterface {

    override fun rate(post: Post, value: Int, onSuccessListener: (Void?) -> Unit) =
        service.rate(post, value, onSuccessListener)

    override fun get(post: Post) = service.get(post)

    companion object {

        @Volatile
        private var instance: RatingsRepository? = null

        fun getInstance(service: RatingsService) = instance
            ?: synchronized(this) {
                instance ?: RatingsRepository(service).also { instance = it }
            }

    }

}