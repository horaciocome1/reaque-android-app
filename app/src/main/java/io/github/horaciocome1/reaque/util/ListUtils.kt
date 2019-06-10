package io.github.horaciocome1.reaque.util

import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

val List<Any>.isListOfTopics: Boolean
    get() {
        if (isNotEmpty())
            if (this[0] is Topic)
                return true
        return false
    }

val List<Any>.isListOfPosts: Boolean
    get() {
        if (isNotEmpty())
            if (this[0] is Post)
                return true
        return false
    }

val List<Any>.isListOfUsers: Boolean
    get() {
        if (isNotEmpty())
            if (this[0] is User)
                return true
        return false
    }

val List<Any>.isListOfNotifications: Boolean
    get() {
        if (isNotEmpty())
            if (this[0] is Notification)
                return true
        return false
    }