package io.github.horaciocome1.reaque.data.readings

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.mapSimple

class ReadingsService : ReadingsInterface {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val increment: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("readings" to increment)
    }

    override fun read(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (post.id.isNotBlank() && auth.currentUser != null) {
            val readingRef = db.document("users/${auth.currentUser!!.uid}/readings/${post.id}")
            val postRef = db.document("posts/${post.id}")
            db.runBatch {
                it.set(readingRef, post.mapSimple)
                it.set(postRef, increment, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

}