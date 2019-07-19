package io.github.horaciocome1.reaque.data.feed

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.posts.Post

interface FeedInterface {

    fun get(): LiveData<List<Post>>

}