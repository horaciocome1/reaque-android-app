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

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

val DocumentSnapshot.user: User
    get() = User(id).apply {
        name = this@user["name"].toString()
        bio = this@user["bio"].toString()
        pic = this@user["pic"].toString()
        address = this@user["address"].toString()
        email = this@user["email"].toString()
        val stamp = this@user["since"]
        if (stamp is Timestamp)
            timestamp = stamp
        posts = this@user["posts"].toString()
        subscribers = this@user["subscribers"].toString()
        subscriptions = this@user["subscriptions"].toString()
        topTopic = this@user["top_topic"].toString()
        val score = this@user["score"]
        if (score != null)
            this.score = score.toString().toFloat()
    }

val DocumentSnapshot.topic: Topic
    get() = Topic(id).apply {
        title = this@topic["title"].toString()
        pic = this@topic["pic"].toString()
    }

val DocumentSnapshot.post: Post
    get() = Post(id).apply {
        pic = this@post["pic"].toString()
        val stamp = this@post["timestamp"]
        if (stamp is Timestamp)
            timestamp = stamp
        message = this@post["message"].toString()
        title = this@post["title"].toString()
        topic = Topic(this@post["topic.id"].toString())
        user.apply {
            id = this@post["user.id"].toString()
            name = this@post["user.name"].toString()
            pic = this@post["user.pic"].toString()
        }
        val score = this@post["score"]
        if (score != null)
            this.score = score.toString().toFloat()
        rating = this@post["rating"].toString()
    }