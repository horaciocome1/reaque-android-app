package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.posts.Post

interface RatingsInterface {

    fun set(post: Post, value: Int, onSuccessListener: (Unit?) -> Unit)

    fun get(post: Post): LiveData<Int>

}