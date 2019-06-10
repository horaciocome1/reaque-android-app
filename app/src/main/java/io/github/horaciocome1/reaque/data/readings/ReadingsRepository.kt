package io.github.horaciocome1.reaque.data.readings

import io.github.horaciocome1.reaque.data.posts.Post

class ReadingsRepository private constructor(private val service: ReadingsService) : ReadingsServiceInterface {

    override fun read(post: Post) = service.read(post)

    companion object {

        @Volatile
        private var instance: ReadingsRepository? = null

        fun getInstance(service: ReadingsService) = instance
            ?: synchronized(this) {
                instance ?: ReadingsRepository(service).also { instance = it }
            }

    }

}