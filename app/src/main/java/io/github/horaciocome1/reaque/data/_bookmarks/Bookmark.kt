package io.github.horaciocome1.reaque.data._bookmarks

import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User

data class Bookmark(var id: String) {

    var post = Post("")
    var user = User("")

}