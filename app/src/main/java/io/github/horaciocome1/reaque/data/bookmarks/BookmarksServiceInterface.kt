package io.github.horaciocome1.reaque.data.bookmarks

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.posts.Post

interface BookmarksServiceInterface {

    fun bookmark(post: Post, onSuccessListener: (Void) -> Unit)

    fun unBookmark(post: Post, onSuccessListener: (Void) -> Unit)

    fun get(): LiveData<List<Post>>

    fun isBookmarked(post: Post): LiveData<Boolean>

}