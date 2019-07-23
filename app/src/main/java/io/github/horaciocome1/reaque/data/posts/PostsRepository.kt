package io.github.horaciocome1.reaque.data.posts

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

class PostsRepository private constructor(private val service: PostsService) : PostsInterface {

    override fun create(post: Post, onCompleteListener: (Task<Void?>?) -> Unit) =
        service.create(post, onCompleteListener)

    override fun get(post: Post) = service.get(post)

    override fun get(user: User) = service.get(user)

    override fun get(topic: Topic) = service.get(topic)

    override fun getTop10() = service.getTop10()

    companion object {

        @Volatile
        private var instance: PostsRepository? = null

        fun getInstance(service: PostsService) = instance
            ?: synchronized(this) {
                instance ?: PostsRepository(service).also { instance = it }
            }

    }

}