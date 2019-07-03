package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class SharesService : SharesServiceInterface {

    private val ref = FirebaseFirestore.getInstance().collection("shares")

    private val auth = FirebaseAuth.getInstance()

    override fun share(post: Post, onSuccessListener: (Void) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            ref.document("${user.uid}_${post.id}").get().addOnSuccessListener {
                if (it == null) addToDatabase(post, user)
            }
        }
    }

    private fun addToDatabase(post: Post, user: FirebaseUser) {
        val share = Share("").apply {
            this.post = post
            this.user = user.user
        }
        ref.document("${user.uid}_${post.id}").set(share.map)
    }

}