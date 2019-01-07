/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
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

package io.github.horaciocome1.reeque.data.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reeque.data.topics.Topic
import io.github.horaciocome1.reeque.data.users.User
import io.github.horaciocome1.reeque.utilities.onListenFailed
import io.github.horaciocome1.reeque.utilities.onSnapshotNull
import io.github.horaciocome1.reeque.utilities.toPost

class PostDAO {

    private val tag = "PostDAO"

    private var postList = mutableListOf<Post>()
    private var userPostList = mutableListOf<Post>()

    private val posts = MutableLiveData<List<Post>>()
    private val post = MutableLiveData<Post>()
    private val userPosts = MutableLiveData<List<Post>>()

    private val reference = FirebaseFirestore.getInstance().collection("posts")

    private val topic = Topic("")
    private val user = User("")

    fun addPost(post: Post) {
        postList.add(post)
        posts.value = postList
    }

    fun getPosts(topic: Topic): LiveData<List<Post>> {
        if (postList.isEmpty() or !this.topic.id.equals(topic.id, true)) {
            reference.whereEqualTo(topic.id, true).addSnapshotListener {
                snapshot, exception -> when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        postList = mutableListOf()
                        for (doc in snapshot.documents)
                            postList.add(doc.toPost())
                        posts.value = postList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
            this.topic.id = topic.id
        }
        return posts
    }

    fun getPosts(topic: Topic, user: User): LiveData<List<Post>> {
        if (userPostList.isEmpty() or !this.user.id.equals(user.id, false)) {
            reference.whereEqualTo("writer_id", user.id).addSnapshotListener {
                snapshot, exception -> when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        userPostList = mutableListOf()
                        for (doc in snapshot.documents)
                            userPostList.add(doc.toPost())
                        userPosts.value = userPostList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
            this.user.id = user.id
        }
        return userPosts
    }

    fun getPosts(post: Post): LiveData<Post> {
        reference.document(post.id).addSnapshotListener {
            snapshot, exception -> when {
                exception != null -> onListenFailed(tag, exception)
                snapshot != null -> this.post.value = snapshot.toPost()
                else -> onSnapshotNull(tag)
            }
        }
        return this.post
    }

}
