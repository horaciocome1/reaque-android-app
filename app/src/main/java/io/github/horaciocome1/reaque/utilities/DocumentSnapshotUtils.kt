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

import com.google.firebase.firestore.DocumentSnapshot
import io.github.horaciocome1.reaque.data.comments.Comment
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

val DocumentSnapshot.user: User
    get() {
        return User(id).apply {
            name = this@user["name"].toString()
            description = this@user["description"].toString()
            pic = this@user["pic"].toString()
            totalFollowers = this@user["total_followers"].toString()
            totalPosts = this@user["total_posts"].toString()
            town = this@user["town"].toString()
            since = this@user["since"].toString()
            email = this@user["email"].toString()
        }
    }

val DocumentSnapshot.topic: Topic
    get() {
        return Topic(id).apply {
            title = this@topic["title"].toString()
            cover = this@topic["cover"].toString()
            description = this@topic["description"].toString()
        }
    }

val DocumentSnapshot.post: Post
    get() {
        return Post(id).apply {
            cover = this@post["cover"].toString()
            date = this@post["date"].toString()
            message = this@post["message"].toString()
            title = this@post["title"].toString()
            user = User(this@post["writer_id"].toString()).apply {
                name = this@post["writer_name"].toString()
                pic = this@post["writer_pic"].toString()
            }
        }
    }

val DocumentSnapshot.comment: Comment
    get() {
        return Comment(id).apply {
            message = this@comment["message"].toString()
            date = this@comment["date"].toString()
            user.apply {
                id = this@comment["writer_id"].toString()
                name = this@comment["writer_name"].toString()
                pic = this@comment["writer_pic"].toString()
            }
        }
    }

val DocumentSnapshot.notification: Notification
    get() {
        return Notification(id).apply {
            message = this@notification["message"].toString()
            date = this@notification["date"].toString()
            cover = this@notification["cover"].toString()
            topicId = this@notification["topic_id"].toString()
            postId = this@notification["post_id"].toString()
            userId = this@notification["user_id"].toString()
        }
    }
