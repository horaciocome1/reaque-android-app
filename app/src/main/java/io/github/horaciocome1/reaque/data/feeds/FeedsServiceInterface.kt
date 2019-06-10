package io.github.horaciocome1.reaque.data.feeds

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.posts.Post

interface FeedsServiceInterface {

    fun get(): LiveData<List<Post>>

    fun requestFeed()

}