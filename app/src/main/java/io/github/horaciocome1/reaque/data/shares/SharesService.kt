package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener

class SharesService : SharesInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun share(post: Post, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            val ref = db.document("users/${it.uid}/readings/${post.id}")
            ref.set(post, SetOptions.merge())
        }
    }

}