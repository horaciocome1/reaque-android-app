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
    get() {
        return mapOf(
            "title" to title,
            "message" to message,
            "pic" to pic,
            "date" to FieldValue.serverTimestamp(),
            "topic" to mapOf("id" to topic.id),
            "user" to mapOf(
                "id" to user.id,
                "name" to user.name,
                "pic" to user.pic
            )
        )
    }

val User.map: Map<String, Any>
    get() {
        return mapOf(
            "bio" to bio,
            "address" to address
        )
    }

val FirebaseUser.map: Map<String, Any?>
    get() {
        return mapOf(
            "name" to displayName,
            "email" to email,
            "pic" to photoUrl.toString()
        )
    }
