package io.github.horaciocome1.reaque.data.bookmarks

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

class BookmarksRepository private constructor(private val service: BookmarksService) : BookmarksInterface {

    override fun bookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) =
        service.bookmark(post, onCompleteListener)

    override fun unBookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) =
        service.unBookmark(post, onCompleteListener)

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