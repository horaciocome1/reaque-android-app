package io.github.horaciocome1.reaque.data.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.addSimpleSnapshotListener
import io.github.horaciocome1.reaque.util.posts

class FeedService : FeedInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var _posts = mutableListOf<Post>()

    private val posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    override fun get(): LiveData<List<Post>> {
        if (_posts.isEmpty())
            auth.addSimpleAuthStateListener { user ->
                val ref = db.collection("users/${user.uid}/feed")
                ref.orderBy("score", Query.Direction.DESCENDING).addSimpleSnapshotListener {
                    _posts = it.posts
                    posts.value = _posts

                }
            }
        return posts
    }

}