package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.posts.Post

interface RatingsServiceIntarface {

    fun rate(post: Post, value: Int, onSuccessListener: (Void) -> Unit)

    fun get(post: Post): LiveData<Float>

}