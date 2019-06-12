package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.onListeningFailed

class SharesService : SharesServiceInterface {

    private val tag = "SharesService"

    private val ref = FirebaseFirestore.getInstance().collection("shares")

    private val auth = FirebaseAuth.getInstance()

    override fun share(post: Post, onSuccessListener: (Void) -> Unit) {
        auth.addSimpleAuthStateListener {
            val share = Share("").apply {
                this.post = post
                this.user = post.user
            }
            ref.document("${it.uid}_${post.id}").addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListeningFailed(tag, exception)
                    snapshot == null -> ref.document("${it.uid}_${post.id}").set(
                        share.map,
                        SetOptions.merge()
                    ).addOnSuccessListener(onSuccessListener)
                }
            }
        }
    }

}