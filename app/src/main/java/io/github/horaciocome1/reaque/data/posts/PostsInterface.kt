package io.github.horaciocome1.reaque.data.posts

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

interface PostsInterface {

    fun create(post: Post, onCompleteListener: (Task<Void?>?) -> Unit)

    fun get(post: Post): LiveData<Post>

    fun get(user: User): LiveData<List<Post>>

    fun get(topic: Topic): LiveData<List<Post>>

    fun getTop10(): LiveData<List<Post>>

}