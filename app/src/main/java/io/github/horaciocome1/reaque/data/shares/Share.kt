package io.github.horaciocome1.reaque.data.shares

import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User

data class Share(var id: String) {

    var post = Post("")
    var user = User("")

}