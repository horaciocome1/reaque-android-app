package io.github.horaciocome1.reaque.util

import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.data.topics.Topic

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