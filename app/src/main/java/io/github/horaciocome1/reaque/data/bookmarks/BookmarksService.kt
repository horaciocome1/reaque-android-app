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
import io.github.horaciocome1.reaque.util.Constants
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
        MutableLiveData<List<Post>>().apply {
            value = mutableListOf()
        }
    }

    private val isBookmarked: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply {
            value = Constants.States.UNDEFINED
        }
    }

    private val hasBookmarks: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            value = false
        }
    }

    override fun bookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (
            post.id.isNotBlank()
            && auth.currentUser != null
        ) {
            val bookmarkRef = db.document(
                "users/${auth.currentUser!!.uid}" +
                        "/bookmarks/${post.id}"
            )
            val postRef = db.document("posts/${post.id}")
            val userRef = db.document("users/${auth.currentUser!!.uid}")
            db.runBatch {
                it.set(bookmarkRef, post.mapSimple)
                it.set(postRef, increment, SetOptions.merge())
                it.set(userRef, increment, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun unBookmark(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (
            post.id.isNotBlank()
            && auth.currentUser != null
        ) {
            val bookmarkRef = db.document(
                "users/${auth.currentUser!!.uid}" +
                        "/bookmarks/${post.id}"
            )
            val postRef = db.document("posts/${post.id}")
            val userRef = db.document("users/${auth.currentUser!!.uid}")
            db.runBatch {
                it.delete(bookmarkRef)
                it.set(postRef, decrement, SetOptions.merge())
                it.set(userRef, decrement, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun get(): LiveData<List<Post>> {
        posts.value?.let { list ->
            if (
                list.isEmpty()
                && auth.currentUser != null
            )
                db.collection("users/${auth.currentUser!!.uid}/bookmarks")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(100)
                    .get()
                    .addOnSuccessListener {
                        if (it != null)
                            posts.value = it.posts
                    }
        }
        return posts
    }

    override fun isBookmarked(post: Post): LiveData<Int> {
        isBookmarked.value = Constants.States.UNDEFINED
        if (post.id.isNotBlank() && auth.currentUser != null)
            db.document("users/${auth.currentUser!!.uid}/bookmarks/${post.id}")
                .addSnapshotListener { snapshot, exception ->
                    isBookmarked.value = if (
                        exception == null
                        && snapshot != null
                    )
                        if (snapshot.exists())
                            Constants.States.TRUE
                        else
                            Constants.States.FALSE
                    else
                        Constants.States.UNDEFINED
                }
        return isBookmarked
    }

    override fun hasBookmarks(): LiveData<Boolean> {
        hasBookmarks.value = false
        if (auth.currentUser != null)
            db.document("users/${auth.currentUser!!.uid}")
                .addSnapshotListener { snapshot, exception ->
                    if (
                        exception == null
                        && snapshot != null
                        && snapshot.contains("bookmarks")
                    )
                        hasBookmarks.value = snapshot["bookmarks"].toString().toInt() > 0
                }
        return hasBookmarks
    }
}