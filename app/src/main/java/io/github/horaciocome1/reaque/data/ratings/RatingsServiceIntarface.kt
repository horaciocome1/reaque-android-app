package io.github.horaciocome1.reaque.data.ratings

import io.github.horaciocome1.reaque.data.posts.Post

interface RatingsServiceIntarface {

    fun rate(post: Post, value: Int, onSuccessListener: (Void) -> Unit)

}