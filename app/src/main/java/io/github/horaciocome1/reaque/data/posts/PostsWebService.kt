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
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data._users.User
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class PostsWebService {

    private val tag = "PostsWebService"

    /*list of all posts from the same notEmptyTopicsForPosts*/
    private var topicPostsList = mutableListOf<Post>()
    private val topicPosts = MutableLiveData<List<Post>>()

    /*list of all posts from the same user*/
    private var userPostsList = mutableListOf<Post>()
    private val userPosts = MutableLiveData<List<Post>>()

    private val _post = Post("")
    private val post = MutableLiveData<Post>()

    private val ref = FirebaseFirestore.getInstance().collection("posts")

    private val auth = FirebaseAuth.getInstance()

    private var topic = Topic("")
    private var user = User("")

    private var favoritesList = mutableListOf<Post>()
    val favorites = MutableLiveData<List<Post>>()
        get() {
            auth.addSimpleAuthStateListener { user ->
                if (favoritesList.isEmpty()) {
                    ref.whereEqualTo("favorite_for.${user.uid}", true).addSimpleSnapshotListener(tag) {
                        favoritesList = it.posts
                        favorites.value = it.posts
                    }
                }
            }
            return field
        }

    private val isThisMyFavorite = MutableLiveData<Boolean>()

    fun isThisMyFavorite(post: Post): LiveData<Boolean> {
        isThisMyFavorite.value = false
        ref.document(post.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, user ->
            isThisMyFavorite.value = snapshot["favorite_for.${user.uid}"].toString().toBoolean()
        }
        return isThisMyFavorite
    }

    fun submitPost(post: Post, onSuccessListener: () -> Unit) {
        auth.addSimpleAuthStateListener {
            post.user = it.user
            ref.add(post.map).addOnSuccessListener {
                onSuccessListener()
            }
        }
    }

    /*retrieve from remote server all topicPosts from the same notEmptyTopicsForPosts*/
    fun getPosts(topic: Topic): LiveData<List<Post>> {
        if (!this.topic.id.equals(topic.id, true)) {
            topicPosts.value = mutableListOf()
            ref.whereEqualTo("topic.id", topic.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                topicPostsList = snapshot.posts
                topicPosts.value = snapshot.posts
            }
            this.topic.id = topic.id
        }
        return topicPosts
    }

    /*retrieve from remote server every topicPosts wrote by the specified user*/
    fun getPosts(user: User): LiveData<List<Post>> {
        if (!this.user.id.equals(user.id, true)) {
            userPosts.value = mutableListOf()
            ref.whereEqualTo("user.id", user.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                userPostsList = snapshot.posts
                userPosts.value = snapshot.posts
            }
            this.user.id = user.id
        }
        return userPosts
    }

    /*retrieve from remote server the post that the user wants to read*/
    fun getPosts(post: Post): LiveData<Post> {
        if (!post.id.equals(_post.id, true)) {
            this.post.value = Post("")
            ref.document(post.id).addSimpleAndSafeSnapshotListener(tag, auth) { it, _ ->
                this.post.value = it.post
            }
            _post.id = post.id
        }
        return this.post
    }

}
