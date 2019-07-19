package io.github.horaciocome1.reaque.data.ratings

import io.github.horaciocome1.reaque.data.posts.Post

class RatingsRepository private constructor(private val service: RatingsService) : RatingsInterface {

    override fun set(post: Post, value: Int, onSuccessListener: (Unit?) -> Unit) =
        service.set(post, value, onSuccessListener)

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