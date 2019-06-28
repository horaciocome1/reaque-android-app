package io.github.horaciocome1.reaque.data.feeds

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.feeds
import io.github.horaciocome1.reaque.util.onListeningFailed

class FeedsService : FeedsServiceInterface {

    private val tag = "FeedsService"

    private val ref = FirebaseFirestore.getInstance().collection("feeds")
    private val requestRef = FirebaseFirestore.getInstance().collection("feed_requests")

    private val auth = FirebaseAuth.getInstance()

    private var _posts = mutableListOf<Post>()

    private val posts = MutableLiveData<List<Post>>()

    override fun get(): LiveData<List<Post>> {
        if (_posts.isEmpty())
            auth.addSimpleAuthStateListener {
                ref.whereEqualTo("subscriber.id", it.uid).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListeningFailed(tag, exception)
                        snapshot != null -> {
                            _posts = snapshot.feeds
                            posts.value = _posts
                        }
                        else -> requestFeed()
                    }
                }
            }
        return posts
    }

    override fun requestFeed() {
        auth.addSimpleAuthStateListener {
            val request = mapOf(
                "user" to mapOf(
                    "id" to it.uid
                ),
                "timestamp" to FieldValue.serverTimestamp()
            )
            requestRef.add(request)
        }
    }

}