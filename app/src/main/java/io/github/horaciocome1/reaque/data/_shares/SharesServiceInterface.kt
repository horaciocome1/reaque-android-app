package io.github.horaciocome1.reaque.data._shares

import com.google.firebase.firestore.DocumentReference
import io.github.horaciocome1.reaque.data._posts.Post

interface SharesServiceInterface {

    fun share(post: Post, onSuccessListener: (DocumentReference) -> Unit)

}