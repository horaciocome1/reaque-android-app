package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user

class SharesService : SharesServiceInterface {

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("shares") }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun share(post: Post, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener { user ->
            ref.document("${user.uid}_${post.id}").get().addOnSuccessListener {
                val postId = it["post.id"]
                if (postId == null)
                    save(post, user)
            }
        }
    }

    private fun save(post: Post, user: FirebaseUser) {
        val share = Share("").apply {
            this.post = post
            this.user = user.user
        }
        ref.document("${user.uid}_${post.id}").set(share.map)
    }

}