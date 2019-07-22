package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.mapSimple

class SharesService : SharesInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val increment: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("shares" to increment)
    }

    override fun share(post: Post, onSuccessListener: (Unit?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            val readingRef = db.document("users/${user.uid}/shares/${post.id}")
            val postRef = db.document("posts/${post.id}")
            readingRef.get().addOnSuccessListener { snapshot ->
                if (snapshot["timestamp"] == null) {
                    db.runBatch {
                        it.set(readingRef, post.mapSimple)
                        it.set(postRef, increment, SetOptions.merge())
                    }
                }
            }
        }
    }

}