/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.util

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import io.github.horaciocome1.reaque.data._bookmarks.Bookmark
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._ratings.Rating
import io.github.horaciocome1.reaque.data._readings.Reading
import io.github.horaciocome1.reaque.data._shares.Share
import io.github.horaciocome1.reaque.data._subscriptions.Subscription
import io.github.horaciocome1.reaque.data._users.User

val Post.map: Map<String, Any>
    get() = mapOf(
        "title" to title,
        "message" to message,
        "pic" to pic,
        "timestamp" to FieldValue.serverTimestamp(),
        "topic" to mapOf(
            "id" to topic.id
        ),
        "user" to mapOf(
            "id" to user.id,
            "name" to user.name,
            "pic" to user.pic
        )
    )

val Bookmark.map: Map<String, Any>
    get() = mapOf(
        "post" to mapOf(
            "title" to post.title,
            "pic" to post.pic
        ),
        "user" to mapOf(
            "id" to user.id,
            "name" to user.name
        ),
        "timestamp" to FieldValue.serverTimestamp()
    )

val Subscription.map: Map<String, Any>
    get() = mapOf(
        "subscribed" to mapOf(
            "id" to subscribed.id,
            "name" to subscribed.name,
            "pic" to subscribed.pic
        ),
        "subscriber" to mapOf(
            "id" to subscriber.id,
            "name" to subscriber.name,
            "pic" to subscriber.pic
        ),
        "timestamp" to FieldValue.serverTimestamp()
    )

val Rating.map: Map<String, Any>
    get() = mapOf(
        "post" to mapOf(
            "id" to post.id
        ),
        "user" to mapOf(
            "id" to user.id
        ),
        "value" to value,
        "timestamp" to FieldValue.serverTimestamp()
    )

val Reading.map: Map<String, Any>
    get() = mapOf(
        "post" to mapOf(
            "id" to post.id
        ),
        "user" to mapOf(
            "id" to user.id
        ),
        "timestamp" to FieldValue.serverTimestamp()
    )

val Share.map: Map<String, Any>
    get() = mapOf(
        "post" to mapOf(
            "id" to post.id
        ),
        "user" to mapOf(
            "id" to user.id
        ),
        "timestamp" to FieldValue.serverTimestamp()
    )

val User.map: Map<String, Any>
    get() = mapOf(
        "bio" to bio,
        "address" to address
    )

val FirebaseUser.user: User
    get() = User(uid).apply {
        name = displayName.toString()
        pic = photoUrl.toString()
    }
