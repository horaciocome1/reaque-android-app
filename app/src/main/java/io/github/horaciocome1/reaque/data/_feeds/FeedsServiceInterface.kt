package io.github.horaciocome1.reaque.data._feeds

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data._posts.Post

interface FeedsServiceInterface {

    fun get(): LiveData<List<Post>>

    fun requestFeed()

}