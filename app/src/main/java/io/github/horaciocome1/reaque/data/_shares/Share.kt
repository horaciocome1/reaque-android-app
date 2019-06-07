package io.github.horaciocome1.reaque.data._shares

import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User

data class Share(var id: String) {

    var post = Post("")
    var user = User("")

}