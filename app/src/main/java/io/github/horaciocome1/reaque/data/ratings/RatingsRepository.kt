package io.github.horaciocome1.reaque.data.ratings

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

class RatingsRepository private constructor(private val service: RatingsService) : RatingsInterface {

    override fun set(post: Post, value: Int, onCompleteListener: (Task<Unit?>?) -> Unit) =
        service.set(post, value, onCompleteListener)

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