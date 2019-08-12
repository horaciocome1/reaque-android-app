package io.github.horaciocome1.reaque.util

import com.google.firebase.firestore.QuerySnapshot
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

val QuerySnapshot.topics: MutableList<Topic>
    get() {
        val list = mutableListOf<Topic>()
        forEach {
            list.add(it.topic)
        }
        return list
    }

val QuerySnapshot.users: MutableList<User>
    get() {
        val list = mutableListOf<User>()
        forEach {
            list.add(it.user)
        }
        return list
    }

val QuerySnapshot.posts: MutableList<Post>
    get() {
        val list = mutableListOf<Post>()
        forEach {
            list.add(it.post)
        }
        return list
    }