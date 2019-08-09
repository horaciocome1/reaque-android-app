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

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

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

    private val hasBookmarks: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }

    override fun bookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (post.id.isNotBlank())
            auth.addSimpleAuthStateListener { user ->
                val bookmarkRef = db.document("users/${user.uid}/bookmarks/${post.id}")
                val postRef = db.document("posts/${post.id}")
                val userRef = db.document("users/${user.uid}")
                db.runBatch {
                    it.set(bookmarkRef, post.mapSimple)
                    it.set(postRef, increment, SetOptions.merge())
                    it.set(userRef, increment, SetOptions.merge())
                }.addOnCompleteListener(onCompleteListener)
            }
    }

    override fun unBookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (post.id.isNotBlank())
            auth.addSimpleAuthStateListener { user ->
                val bookmarkRef = db.document("users/${user.uid}/bookmarks/${post.id}")
                val postRef = db.document("posts/${post.id}")
                val userRef = db.document("users/${user.uid}")
                db.runBatch {
                    it.delete(bookmarkRef)
                    it.set(postRef, decrement, SetOptions.merge())
                    it.set(userRef, decrement, SetOptions.merge())
                }.addOnCompleteListener(onCompleteListener)
            }
    }

    override fun get(): LiveData<List<Post>> {
        posts.value?.let { list ->
            if (list.isEmpty())
                auth.addSimpleAuthStateListener { user ->
                    db.collection("users/${user.uid}/bookmarks")
                        .orderBy("score", Query.Direction.DESCENDING)
                        .limit(100)
                        .get()
                        .addOnSuccessListener {
                            posts.value = it.posts
                        }
                }
        }
        return posts
    }

    override fun isBookmarked(post: Post): LiveData<Boolean> {
        isBookmarked.value = false
        if (post.id.isNotBlank())
            auth.addSimpleAuthStateListener { user ->
                db.document("users/${user.uid}/bookmarks/${post.id}")
                    .addSimpleSnapshotListener {
                        isBookmarked.value = it["title"] != null
                    }
            }
        return isBookmarked
    }

    override fun hasBookmarks(): LiveData<Boolean> {
        hasBookmarks.value = false
        auth.addSimpleAuthStateListener { user ->
            db.document("users/${user.uid}")
                .addSimpleSnapshotListener { snapshot ->
                    snapshot["bookmarks"]?.let {
                        hasBookmarks.value = it.toString().toInt() > 0
                    }
            }
        }
        return hasBookmarks
    }
}