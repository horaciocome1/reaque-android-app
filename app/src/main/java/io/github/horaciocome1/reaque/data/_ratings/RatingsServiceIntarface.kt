package io.github.horaciocome1.reaque.data._ratings

import io.github.horaciocome1.reaque.data._posts.Post

interface RatingsServiceIntarface {

    fun rate(post: Post, value: Int, onSuccessListener: (Void) -> Unit)

}