package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.firestore.DocumentReference
import io.github.horaciocome1.reaque.data.posts.Post

class SharesRepository private constructor(private val service: SharesService) : SharesServiceInterface {

    override fun share(post: Post, onSuccessListener: (DocumentReference) -> Unit) =
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