package io.github.horaciocome1.reaque.data.readings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map

class ReadingsService : ReadingsInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val increment: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("readings" to increment)
    }

    override fun read(post: Post, onSuccessListener: (Unit?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            val readingRef = db.document("posts/${user.uid}/readings/${post.id}")
            val postRef = db.document("posts/${post.id}")
            readingRef.get().addOnSuccessListener { snapshot ->
                if (snapshot["timestamp"] == null) {
                    val data = user.map.plus("post" to mapOf("id" to post.id))
                    db.runBatch {
                        it.set(readingRef, data)
                        it.set(postRef, increment)
                    }
                }
            }
        }
    }

}