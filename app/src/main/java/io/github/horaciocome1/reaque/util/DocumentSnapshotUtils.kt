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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._topics.Topic
import io.github.horaciocome1.reaque.data._users.User

val DocumentSnapshot.user: User
    get() = User(id).apply {
        name = this@user["name"].toString()
        bio = this@user["bio"].toString()
        pic = this@user["pic"].toString()
        address = this@user["address"].toString()
        email = this@user["email"].toString()
        val timestamp = this@user["since"]
        if (timestamp is Timestamp)
            since = timestamp.string
        favoriteForCount = this@user["favorite_for_count"].toString()
        postsCount = this@user["posts_count"].toString()
    }

val DocumentSnapshot.topicForPosts: Topic
    get() = Topic(id).apply {
        title = this@topicForPosts["title"].toString()
        pic = this@topicForPosts["pic"].toString()
        contentCount = this@topicForPosts["posts_count"].toString()
    }

val DocumentSnapshot.topicForUsers: Topic
    get() = Topic(id).apply {
        title = this@topicForUsers["title"].toString()
        pic = this@topicForUsers["pic"].toString()
        contentCount = this@topicForUsers["users_count"].toString()
    }

val DocumentSnapshot.post: Post
    get() = Post(id).apply {
        pic = this@post["pic"].toString()
        val stamp = this@post["date"]
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
    }

val DocumentSnapshot.bookmark: Post
    get() = Post(id).apply {
        id = this@bookmark["post.id"].toString()
        title = this@bookmark["post.title"].toString()
        pic = this@bookmark["post.pic"].toString()
        user.apply {
            id = this@bookmark["user.id"].toString()
            name = this@bookmark["user.name"].toString()
        }
    }

val DocumentSnapshot.subscribed: User
    get() = User(id).apply {
        id = this@subscribed["subscribed.id"].toString()
        name = this@subscribed["subscribed.name"].toString()
        pic = this@subscribed["subscribed.pic"].toString()
    }

val DocumentSnapshot.subscriber: User
    get() = User(id).apply {
        id = this@subscriber["subscriber.id"].toString()
        name = this@subscriber["subscriber.name"].toString()
        pic = this@subscriber["subscriber.pic"].toString()
    }

fun DocumentReference.addSimpleAndSafeSnapshotListener(
    TAG: String,
    auth: FirebaseAuth,
    listener: (DocumentSnapshot, FirebaseUser) -> Unit
) {
    auth.addSimpleAuthStateListener { user ->
        addSimpleSnapshotListener(TAG) {
            listener(it, user)
        }
    }
}

fun DocumentReference.addSimpleSnapshotListener(TAG: String, listener: (DocumentSnapshot) -> Unit) {
    addSnapshotListener { snapshot, exception ->
        when {
            exception != null -> onListeningFailed(TAG, exception)
            snapshot != null -> listener(snapshot)
            else -> onSnapshotNull(TAG)
        }
    }
}
