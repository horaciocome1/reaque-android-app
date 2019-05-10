package io.github.horaciocome1.reaque.util

import com.google.firebase.firestore.QuerySnapshot
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

val QuerySnapshot.topics: MutableList<Topic>
    get() {
        val list = mutableListOf<Topic>()
        forEach { doc ->
            list.add(doc.topic)
            list.sortBy { it.title }
        }
        return list
    }

val QuerySnapshot.users: MutableList<User>
    get() {
        val list = mutableListOf<User>()
        forEach { doc ->
            list.add(doc.user)
            list.sortBy { it.name }
        }
        return list
    }

val QuerySnapshot.posts: MutableList<Post>
    get() {
        val list = mutableListOf<Post>()
        forEach { doc ->
            list.add(doc.post)
            list.sortByDescending { it.timestamp }
        }
        return list
    }

val QuerySnapshot.notifications: MutableList<Notification>
    get() {
        val list = mutableListOf<Notification>()
        forEach { doc ->
            list.add(doc.notification)
            list.sortByDescending { it.timestamp }
        }
        return list
    }