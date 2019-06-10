package io.github.horaciocome1.reaque.data.readings

import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User

data class Reading(var id: String) {

    var post = Post("")
    var user = User("")

}