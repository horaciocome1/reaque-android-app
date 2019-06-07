package io.github.horaciocome1.reaque.data._shares

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map

class SharesService : SharesServiceInterface {

    private val ref = FirebaseFirestore.getInstance().collection("shares")

    private val auth = FirebaseAuth.getInstance()

    override fun share(post: Post, onSuccessListener: (DocumentReference) -> Unit) {
        auth.addSimpleAuthStateListener {
            val share = Share("").apply {
                this.post = post
                this.user = user
            }
            ref.add(share.map).addOnSuccessListener(onSuccessListener)
        }
    }

}