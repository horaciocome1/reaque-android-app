package io.github.horaciocome1.reaque.data.shares

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

class SharesRepository private constructor(private val service: SharesService) : SharesInterface {

    override fun share(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) =
        service.share(post, onCompleteListener)

    companion object {

        @Volatile
        private var instance: SharesRepository? = null

        fun getInstance(service: SharesService) = instance
            ?: synchronized(this) {
                instance ?: SharesRepository(service).also { instance = it }
            }

    }

}