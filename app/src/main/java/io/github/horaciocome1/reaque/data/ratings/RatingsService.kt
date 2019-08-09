package io.github.horaciocome1.reaque.data.ratings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.addSimpleSnapshotListener
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
        MutableLiveData<Int>().apply { value = 0 }
    }

    override fun set(post: Post, value: Int, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (post.id.isNotBlank() && value >= 1 && value <= 5)
            auth.addSimpleAuthStateListener {
                val data = it.user.map
                    .plus("value" to value)
                    .plus("post" to mapOf("id" to post.id))
                db.document("posts/${post.id}/ratings/${it.uid}")
                    .set(data, SetOptions.merge())
                    .addOnCompleteListener(onCompleteListener)
            }
    }

    override fun get(post: Post): LiveData<Int> {
        rating.value = 0
        if (post.id.isNotBlank())
            auth.addSimpleAuthStateListener { user ->
                db.document("posts/${post.id}/ratings/${user.uid}")
                    .addSimpleSnapshotListener { snapshot ->
                        snapshot["value"]?.let {
                            rating.value = it.toString().toInt()
                        }
                    }
            }
        return rating
    }

}