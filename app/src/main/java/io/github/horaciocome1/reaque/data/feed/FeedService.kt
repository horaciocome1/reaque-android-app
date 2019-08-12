package io.github.horaciocome1.reaque.data.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.posts

class FeedService : FeedInterface {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private var _posts = mutableListOf<Post>()

    private val posts: MutableLiveData<List<Post>> by lazy {
        MutableLiveData<List<Post>>().apply { value = mutableListOf() }
    }

    override fun get(): LiveData<List<Post>> {
        if (_posts.isEmpty() && auth.currentUser != null)
            db.collection("users/${auth.currentUser!!.uid}/feed")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(100)
                    .get()
                    .addOnSuccessListener {
                        _posts = it.posts
                        posts.value = _posts
                    }
        return posts
    }

}