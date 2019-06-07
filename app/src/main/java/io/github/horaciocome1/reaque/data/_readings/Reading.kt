package io.github.horaciocome1.reaque.data._readings

import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User

data class Reading(var id: String) {

    var post = Post("")
    var user = User("")

}