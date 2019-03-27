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

package io.github.horaciocome1.reaque.utilities

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import io.github.horaciocome1.reaque.data.comments.Comment
import io.github.horaciocome1.reaque.data.posts.Post

val Post.hashMap: HashMap<String, Any>
    get() = HashMap<String, Any>().apply {
        put("title", title)
        put("message", message)
        put("pic", pic)
        put("date", FieldValue.serverTimestamp())
        put("topic", HashMap<String, Any>().apply {
            put("id", topic.id)
        })
        put("user", HashMap<String, Any>().apply {
            put("id", user.id)
            put("name", user.name)
            put("pic", user.pic)
        })
    }

val FirebaseUser.hashMap: HashMap<String, Any?>
    get() = HashMap<String, Any?>().apply {
        put("name", displayName)
        put("email", email)
        put("pic", photoUrl.toString())
    }

val Comment.hashMap: HashMap<String, Any>
    get() = HashMap<String, Any>().apply {
        put("message", message)
        put("date", FieldValue.serverTimestamp())
        put("topic", HashMap<String, Any>().apply {
            put("id", topic.id)
        })
        put("post", HashMap<String, Any>().apply {
            put("id", post.id)
        })
        put("user", HashMap<String, Any>().apply {
            put("id", user.id)
            put("name", user.name)
            put("pic", user.pic)
        })
    }