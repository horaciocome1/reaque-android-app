package io.github.horaciocome1.reaque.data.shares

import io.github.horaciocome1.reaque.data.posts.Post

interface SharesServiceInterface {

    fun share(post: Post, onSuccessListener: (Void?) -> Unit)

}