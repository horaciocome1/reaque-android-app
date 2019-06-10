package io.github.horaciocome1.reaque.data.ratings

import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User

data class Rating(var id: String) {

    var post = Post("")
    var user = User("")
    var value = 0

}