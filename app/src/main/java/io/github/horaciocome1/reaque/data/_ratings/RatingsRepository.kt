package io.github.horaciocome1.reaque.data._ratings

import io.github.horaciocome1.reaque.data._posts.Post

class RatingsRepository private constructor(private val service: RatingsService) : RatingsServiceIntarface {

    override fun rate(post: Post, value: Int, onSuccessListener: (Void) -> Unit) =
        service.rate(post, value, onSuccessListener)

    companion object {

        @Volatile
        private var instance: RatingsRepository? = null

        fun getInstance(service: RatingsService) = instance
            ?: synchronized(this) {
                instance ?: RatingsRepository(service).also { instance = it }
            }

    }

}