package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.addSimpleSnapshotListener
import io.github.horaciocome1.reaque.util.mapRating

class RatingsService : RatingsInterface {

    private val tag: String by lazy { "RatingsService" }

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val rating: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply { value = 1 }
    }

    override fun rate(post: Post, value: Int, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            val ref = db.document("users/${user.uid}/ratings/${post.id}/requests/${post.id}")
            ref.set(post.mapRating((value)), SetOptions.merge())
        }
    }

    override fun get(post: Post): LiveData<Int> {
        rating.value = 1
        auth.addSimpleAuthStateListener { user ->
            val ref = db.document("users/${user.uid}/ratings/${post.id}")
            ref.addSimpleSnapshotListener(tag) {
                val value = it["value"]
                if (value != null)
                    rating.value = value.toString().toInt()
                else
                    rating.value = 1
            }
        }
        return rating
    }

}