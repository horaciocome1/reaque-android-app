package io.github.horaciocome1.reaque.data.readings

import io.github.horaciocome1.reaque.data.posts.Post

interface ReadingsServiceInterface {

    fun read(post: Post, onSuccessListener: (Void?) -> Unit)

}