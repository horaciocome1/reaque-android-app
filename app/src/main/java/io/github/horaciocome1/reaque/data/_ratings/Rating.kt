package io.github.horaciocome1.reaque.data._ratings

import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User

data class Rating(var id: String) {

    var post = Post("")
    var user = User("")
    var value = 0

}