package io.github.horaciocome1.reaque.data._bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.util.*

class BookmarksService : BookmarksServiceInterface {

    private val tag = "BookmarksService"

    private val ref = FirebaseFirestore.getInstance().collection("bookmarks")

    private val auth = FirebaseAuth.getInstance()

    private val bookmarks = MutableLiveData<List<Bookmark>>().apply { value = mutableListOf() }

    private val isBookmarked = MutableLiveData<Boolean>().apply { value = false }

    override fun bookmark(post: Post, onSuccessListener: (Void) -> Unit) {
        auth.addSimpleAuthStateListener {
            val bookmark = Bookmark("").apply {
                this.post = post
                this.user = it.user
            }
            ref.document("${it.uid}_${post.id}").set(bookmark.map).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun unBookmark(post: Post, onSuccessListener: (Void) -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document("${it.uid}_${post.id}").delete().addOnSuccessListener(onSuccessListener)
        }
    }

    override fun get(): LiveData<List<Bookmark>> {
        bookmarks.value?.let {
            if (it.isEmpty())
                auth.addSimpleAuthStateListener { user ->
                    ref.whereEqualTo("user.id", user.uid).addSimpleSnapshotListener(tag) { snapshot ->
                        bookmarks.value = snapshot.bookmarks
                    }
                }
        }
        return bookmarks
    }

    override fun isBookmarked(post: Post): LiveData<Boolean> {
        isBookmarked.value = false
        auth.addSimpleAuthStateListener {
            ref.document("${it.uid}_${post.id}").addSimpleSnapshotListener(tag) {
                // if this is called, document exists
                isBookmarked.value = true
            }
        }
        return isBookmarked
    }

}