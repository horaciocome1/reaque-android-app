package io.github.horaciocome1.reaque.data.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.addSimpleSnapshotListener
import io.github.horaciocome1.reaque.util.mapSimple
import io.github.horaciocome1.reaque.util.posts

class BookmarksService : BookmarksInterface {

    private val tag: String by lazy { "BookmarksService" }

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val increment: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("bookmarks" to increment)
    }

    private val decrement: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(-1)
        mapOf("bookmarks" to increment)
    }

    private val posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    private val isBookmarked: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }

    override fun bookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            val bookmarkRef = db.document("users/${user.uid}/bookmarks/${post.id}")
            val postRef = db.document("posts/${post.id}")
            db.runBatch {
                it.set(bookmarkRef, post.mapSimple)
                it.set(postRef, increment, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun unBookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            val bookmarkRef = db.document("users/${user.uid}/bookmarks/${post.id}")
            val postRef = db.document("posts/${post.id}")
            db.runBatch {
                it.delete(bookmarkRef)
                it.set(postRef, decrement, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun get(): LiveData<List<Post>> {
        posts.value?.let {
            if (it.isEmpty())
                auth.addSimpleAuthStateListener { user ->
                    val ref = db.collection("users/${user.uid}/bookmarks")
                    ref.orderBy("score", Query.Direction.DESCENDING).addSimpleSnapshotListener(tag) { snapshot ->
                        posts.value = snapshot.posts
                    }
                }
        }
        return posts
    }

    override fun isBookmarked(post: Post): LiveData<Boolean> {
        isBookmarked.value = false
        auth.addSimpleAuthStateListener { user ->
            val ref = db.document("users/${user.uid}/bookmarks/${post.id}")
            ref.addSimpleSnapshotListener(tag) {
                isBookmarked.value = it["title"] != null
            }
        }
        return isBookmarked
    }

}