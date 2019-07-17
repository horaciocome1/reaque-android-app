package io.github.horaciocome1.reaque.data.readings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map

class ReadingsService : ReadingsInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun read(post: Post, onSuccessListener: (Unit?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            db.runTransaction {
                val ref = db.document("posts/${post.id}/readings/${user.uid}")
                val snapshot = it[ref]
                if (snapshot["name"] == null) {
                    val map = user.map.plus("post" to mapOf("id" to post.id))
                    it.set(ref, map)
                }
            }.addOnSuccessListener(onSuccessListener)
        }
    }

}