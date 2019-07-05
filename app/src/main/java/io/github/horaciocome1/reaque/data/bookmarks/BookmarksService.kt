package io.github.horaciocome1.reaque.data.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.*

class BookmarksService : BookmarksServiceInterface {

    private val tag: String by lazy { "BookmarksService" }

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("bookmarks") }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    private val isBookmarked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }

    override fun bookmark(post: Post, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            val bookmark = Bookmark("").apply {
                this.post = post
                this.user = it.user
            }
            ref.document("${it.uid}_${post.id}").set(bookmark.map).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun unBookmark(post: Post, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document("${it.uid}_${post.id}").delete().addOnSuccessListener(onSuccessListener)
        }
    }

    override fun get(): LiveData<List<Post>> {
        posts.value?.let {
            if (it.isEmpty())
                auth.addSimpleAuthStateListener { user ->
                    ref.whereEqualTo("user.id", user.uid).addSimpleSnapshotListener(tag) { snapshot ->
                        posts.value = snapshot.bookmarks
                    }
                }
        }
        return posts
    }

    override fun isBookmarked(post: Post): LiveData<Boolean> {
        isBookmarked.value = false
        auth.addSimpleAuthStateListener { user ->
            ref.document("${user.uid}_${post.id}").addSimpleSnapshotListener(tag) {
                val postId = it["post.id"]
                isBookmarked.value = postId != null
            }
        }
        return isBookmarked
    }

}