package io.github.horaciocome1.reaque.data.bookmarks

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.posts.Post

interface BookmarksInterface {

    fun bookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit)

    fun unBookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit)

    fun get(): LiveData<List<Post>>

    fun isBookmarked(post: Post): LiveData<Boolean>

}