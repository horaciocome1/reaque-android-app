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
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User

fun DocumentSnapshot.toUser() = User(id).apply {
    name = this@toUser["name"].toString()
    description = this@toUser["description"].toString()
    pic = this@toUser["pic"].toString()
    totalFollowers = this@toUser["total_followers"].toString()
    totalPosts = this@toUser["total_posts"].toString()
    topics = this@toUser["topics"].toString()
    town = this@toUser["town"].toString()
    since = this@toUser["since"].toString()
    email = this@toUser["email"].toString()
}

fun DocumentSnapshot.toTopic() = Topic(id).apply {
    title = this@toTopic["title"].toString()
    totalReaders = this@toTopic["totalReaders"].toString().toInt()
    cover = this@toTopic["cover"].toString()
//    for (i in 1 until 4)
//        posts.add(
//            Post(this@toTopic["post${i}_id"].toString()).apply {
//                rating = this@toTopic["post${i}_rating"].toString().toFloat()
//                title = this@toTopic["post${i}_title"].toString()
//                user = User(this@toTopic["post${i}_writer_id"].toString()).apply {
//                    pic = this@toTopic["post${i}_writer_pic"].toString()
//                }
//            }
//        )
}

fun DocumentSnapshot.toPost() = Post(id).apply {
    cover = this@toPost["cover"].toString()
    date = this@toPost["date"].toString()
    message = this@toPost["message"].toString()
    rating = this@toPost["rating"].toString().toFloat()
    title = this@toPost["title"].toString()
    topic = this@toPost["topic"].toString()
    user = User(this@toPost["writerId"].toString()).apply {
        name = this@toPost["writer_name"].toString()
        pic = this@toPost["writer_pic"].toString()
    }
}

fun DocumentSnapshot.toComment() = Comment(id).apply {
    message = this@toComment["message"].toString()
    date = this@toComment["date"].toString()
    totalLikes = this@toComment["total_likes"].toString().toInt()
    writerId = this@toComment["writer_id"].toString()
    writerName = this@toComment["writer_name"].toString()
    writerPic = this@toComment["writer_pic"].toString()
}