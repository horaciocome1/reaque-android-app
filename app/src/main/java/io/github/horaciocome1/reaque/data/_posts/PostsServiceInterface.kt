package io.github.horaciocome1.reaque.data._posts

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import io.github.horaciocome1.reaque.data._users.User
import io.github.horaciocome1.reaque.data.topics.Topic

interface PostsServiceInterface {

    fun create(post: Post, onSuccessListener: (DocumentReference) -> Unit)

    fun get(post: Post): LiveData<Post>

    fun get(user: User): LiveData<List<Post>>

    fun get(topic: Topic): LiveData<List<Post>>

}