package io.github.horaciocome1.reaque.data._readings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class ReadingsService : ReadingsServiceInterface {

    private val ref = FirebaseFirestore.getInstance().collection("readings")

    private val auth = FirebaseAuth.getInstance()

    override fun read(post: Post) {
        auth.addSimpleAuthStateListener {
            val reading = Reading("").apply {
                this.post = post
                user = it.user
            }
            ref.add(reading.map)
        }
    }

}