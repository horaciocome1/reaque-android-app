package io.github.horaciocome1.reaque.data.readings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class ReadingsService : ReadingsServiceInterface {

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("readings") }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun read(post: Post, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            val id = "${user.uid}_${post.id}"
            ref.document(id).get().addOnSuccessListener {
                val postId = it["post.id"]
                if (postId == null)
                    save(post, user, onSuccessListener)
            }
        }
    }

    private fun save(post: Post, user: FirebaseUser, onSuccessListener: (Void?) -> Unit) {
        val reading = Reading("").apply {
            this.post = post
            this.user = user.user
        }
        ref.document("${user.uid}_${post.id}").set(reading.map).addOnSuccessListener(onSuccessListener)
    }

}