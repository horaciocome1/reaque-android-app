package io.github.horaciocome1.reaque.data.readings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class ReadingsService : ReadingsServiceInterface {

    private val ref = FirebaseFirestore.getInstance().collection("readings")

    private val auth = FirebaseAuth.getInstance()

    override fun read(post: Post) {
        auth.addSimpleAuthStateListener { user ->
            val id = "${user.uid}_${post.id}"
            ref.document(id).get().addOnSuccessListener {
                if (it == null) addToDatabase(post, user)
            }
        }
    }

    private fun addToDatabase(post: Post, user: FirebaseUser) {
        val reading = Reading("").apply {
            this.post = post
            this.user = user.user
        }
        ref.document("${user.uid}_${post.id}").set(reading.map)
    }

}