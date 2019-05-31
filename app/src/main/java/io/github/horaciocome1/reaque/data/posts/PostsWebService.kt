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

package io.github.horaciocome1.reaque.data.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.*

class PostsWebService {

    private val tag = "PostsWebService"

    /*list of all posts from the same notEmptyTopics*/
    private var topicPostsList = mutableListOf<Post>()
    private val topicPosts = MutableLiveData<List<Post>>()

    /*list of all posts from the same user*/
    private var userPostsList = mutableListOf<Post>()
    private val userPosts = MutableLiveData<List<Post>>()

    private val _post = Post("")
    private val post = MutableLiveData<Post>()

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("posts")

    private val auth = FirebaseAuth.getInstance()

    private var topic = Topic("")
    private var user = User("")

    private var favoritesList = mutableListOf<Post>()
    val favorites = MutableLiveData<List<Post>>()
        get() {
            auth.addSimpleAuthStateListener {
                if (favoritesList.isEmpty()) {
                    ref.whereEqualTo("favorite_for.${it.uid}", true).addSnapshotListener { snapshot, exception ->
                        when {
                            exception != null -> onListenFailed(tag, exception)
                            snapshot != null -> {
                                favoritesList = snapshot.posts
                                field.value = favoritesList
                            }
                            else -> onSnapshotNull(tag)
                        }
                    }
                }
            }
            return field
        }

    val isThisMyFavorite = MutableLiveData<Boolean>()
        get() {
            field.value = false
            auth.addSimpleAuthStateListener {
                ref.document(_post.id).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> field.value = snapshot["favorite_for.${it.uid}"].toString().toBoolean()
                        else -> onSnapshotNull(tag)
                    }
                }
            }
            return field
        }

    fun submitPost(post: Post, onSuccessful: () -> Unit) {
        auth.addSimpleAuthStateListener {
            post.user.run {
                id = it.uid
                name = it.displayName.toString()
                pic = it.photoUrl.toString()
            }
            ref.add(post.map).addOnSuccessListener {
                onSuccessful()
            }
        }
    }

    /*retrieve from remote server all topicPosts from the same notEmptyTopics*/
    fun getPosts(topic: Topic): LiveData<List<Post>> {
        auth.addSimpleAuthStateListener {
            if (!this.topic.id.equals(topic.id, true)) {
                topicPosts.value = mutableListOf()
                ref.whereEqualTo("topic.id", topic.id).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            topicPostsList = snapshot.posts
                            topicPosts.value = topicPostsList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
                this.topic.id = topic.id
            }
        }
        return topicPosts
    }

    /*retrieve from remote server every topicPosts wrote by the specified user*/
    fun getPosts(user: User): LiveData<List<Post>> {
        auth.addSimpleAuthStateListener {
            if (!this.user.id.equals(user.id, true)) {
                userPosts.value = mutableListOf()
                ref.whereEqualTo("user.id", user.id).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            userPostsList = snapshot.posts
                            userPosts.value = userPostsList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
                this.user.id = user.id
            }
        }
        return userPosts
    }

    /*retrieve from remote server the post that the user wants to read*/
    fun getPosts(post: Post): LiveData<Post> {
        auth.addSimpleAuthStateListener {
            if (!post.id.equals(_post.id, true)) {
                this.post.value = Post("")
                ref.document(post.id).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> this.post.value = snapshot.post
                        else -> onSnapshotNull(tag)
                    }
                }
                _post.id = post.id
            }
        }
        return this.post
    }

}
