package io.github.horaciocome1.reaque.data.bookmarks

import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User

data class Bookmark(var id: String) {

    var post = Post("")
    var user = User("")

}