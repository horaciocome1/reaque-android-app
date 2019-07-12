package io.github.horaciocome1.reaque.data.shares

import io.github.horaciocome1.reaque.data.posts.Post

class SharesRepository private constructor(private val service: SharesService) : SharesInterface {

    override fun share(post: Post, onSuccessListener: (Void?) -> Unit) =
        service.share(post, onSuccessListener)

    companion object {

        @Volatile
        private var instance: SharesRepository? = null

        fun getInstance(service: SharesService) = instance
            ?: synchronized(this) {
                instance ?: SharesRepository(service).also { instance = it }
            }

    }

}