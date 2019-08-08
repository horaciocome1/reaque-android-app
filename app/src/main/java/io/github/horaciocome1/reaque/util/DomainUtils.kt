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
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User

val Post.map: Map<String, Any>
    get() = mapOf(
        "title" to title,
        "message" to message,
        "pic" to pic,
        "timestamp" to timestamp,
        "bookmarks" to 0,
        "readings" to 0,
        "rating" to 0,
        "shares" to 0,
        "score" to 0,
        "topic" to mapOf(
            "id" to topic.id
        ),
        "user" to mapOf(
            "id" to user.id,
            "name" to user.name,
            "pic" to user.pic
        )
    )

val Post.mapSimple: Map<String, Any>
    get() = mapOf(
        "id" to id,
        "title" to title,
        "pic" to pic,
        "timestamp" to timestamp,
        "topic" to mapOf(
            "id" to topic.id
        ),
        "user" to mapOf(
            "id" to user.id,
            "name" to user.name,
            "pic" to user.pic
        ),
        "score" to score
    )

val User.map: Map<String, Any>
    get() = mapOf(
        "id" to id,
        "name" to name,
        "pic" to pic,
        "subscribers" to subscribers,
        "top_topic" to topTopic,
        "score" to score,
        "timestamp" to FieldValue.serverTimestamp()
    )

val User.mapSimple: Map<String, Any>
    get() = mapOf(
        "bio" to bio,
        "address" to address
    )

val FirebaseUser.user: User
    get() = User(uid).apply {
        name = displayName.toString()
        pic = photoUrl.toString()
    }