package io.github.horaciocome1.reaque.data.shares

import com.google.firebase.firestore.DocumentReference
import io.github.horaciocome1.reaque.data.posts.Post

interface SharesServiceInterface {

    fun share(post: Post, onSuccessListener: (DocumentReference) -> Unit)

}