package io.github.horaciocome1.reaque.data.bookmarks

import io.github.horaciocome1.reaque.data.posts.Post

class BookmarksRepository private constructor(private val service: BookmarksService) : BookmarksInterface {

    override fun bookmark(post: Post, onSuccessListener: (Void?) -> Unit) = service.bookmark(post, onSuccessListener)

    override fun unBookmark(post: Post, onSuccessListener: (Void?) -> Unit) =
        service.unBookmark(post, onSuccessListener)

    override fun get() = service.get()

    override fun isBookmarked(post: Post) = service.isBookmarked(post)

    companion object {

        @Volatile
        private var instance: BookmarksRepository? = null

        fun getInstance(service: BookmarksService) = instance
            ?: synchronized(this) {
                instance ?: BookmarksRepository(service).also { instance = it }
            }

    }

}