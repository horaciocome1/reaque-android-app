package io.github.horaciocome1.reaque.data.readings

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

class ReadingsRepository private constructor(
    private val service: ReadingsService
) : ReadingsInterface {

    override fun read(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) = service.read(post, onCompleteListener)

    companion object {

        @Volatile
        private var instance: ReadingsRepository? = null

        fun getInstance(service: ReadingsService) = instance ?: synchronized(this) {
            instance ?: ReadingsRepository(service)
                .also {
                    instance = it
                }
        }

    }

}