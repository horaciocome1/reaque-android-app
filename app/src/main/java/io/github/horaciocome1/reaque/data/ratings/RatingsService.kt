package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class RatingsService : RatingsInterface {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val rating: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().apply {
            value = 0
        }
    }

    override fun set(post: Post, value: Int, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (
            post.id.isNotBlank()
            && value >= 1
            && value <= 5
            && auth.currentUser != null
        ) {
            val data = auth.currentUser!!.user.map
                .plus("value" to value)
                .plus(
                    "post" to mapOf(
                        "id" to post.id
                    )
                )
            db.document(
                "posts/${post.id}" +
                        "/ratings/${auth.currentUser!!.uid}"
            )
                .set(data, SetOptions.merge())
                .addOnCompleteListener(onCompleteListener)
        }
    }

    override fun get(post: Post): LiveData<Int> {
        rating.value = 0
        if (post.id.isNotBlank() && auth.currentUser != null)
            db.document(
                "posts/${post.id}" +
                        "/ratings/${auth.currentUser!!.uid}"
            )
                .addSnapshotListener { snapshot, exception ->
                    if (
                        exception == null
                        && snapshot != null
                        && snapshot.contains("value")
                    )
                        rating.value = snapshot["value"]
                            .toString()
                            .toInt()
                }
        return rating
    }

}