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
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.onListenFailed
import io.github.horaciocome1.reaque.util.onSnapshotNull
import io.github.horaciocome1.reaque.util.post

class PostsWebService {

    private val tag = "PostsWebService"
    private val userIdPath = "user.id"
    private val topicIdPath = "topic.id"

    /*list of all posts from the same topics*/
    private var topicPostsList = mutableListOf<Post>()
    private val topicPosts = MutableLiveData<List<Post>>()

    /*list of all posts from the same user*/
    private var userPostsList = mutableListOf<Post>()
    private val userPosts = MutableLiveData<List<Post>>()

    private val post = MutableLiveData<Post>().apply {
        value = Post("")
    }

    /*list of all posts from the same user*/
    private var favoritesList = mutableListOf<Post>()
    private val favorites = MutableLiveData<List<Post>>()

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("posts")

    private lateinit var auth: FirebaseAuth

    private var topicId = ""
    private var userId = ""

    private val isThisMyFavorite = MutableLiveData<Boolean>().apply { value = false }

    fun submitPost(post: Post, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        post.user.run {
            auth.currentUser?.let {
                id = it.uid
                name = it.displayName!!
                pic = it.photoUrl.toString()
            }
        }
        ref.add(post.map).addOnCompleteListener {
            if (it.isSuccessful)
                onSuccessful()
        }
    }

    /*retrieve from remote server all topicPosts from the same topics*/
    fun getPosts(topic: Topic): LiveData<List<Post>> {
        if (!topicId.equals(topic.id, true)) {
            topicPosts.value = mutableListOf()
            ref.whereEqualTo(topicIdPath, topic.id)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            topicPostsList = mutableListOf()
                            topicPosts.value = topicPostsList.apply {
                                for (doc in snapshot)
                                    add(doc.post)
                                sortByDescending { it.timestamp }
                            }
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
            topicId = topic.id
        }
        return topicPosts
    }

    /*retrieve from remote server every topicPosts wrote by the specified user*/
    fun getPosts(user: User): LiveData<List<Post>> {
        if (!userId.equals(user.id, true)) {
            userPosts.value = mutableListOf()
            ref.whereEqualTo(userIdPath, user.id)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            userPostsList = mutableListOf()
                            userPosts.value = userPostsList.apply {
                                for (doc in snapshot)
                                    add(doc.post)
                                sortByDescending { it.timestamp }
                            }
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
            userId = user.id
        }
        return userPosts
    }

    /*retrieve from remote server the post that the user wants to read*/
    fun getPosts(post: Post): LiveData<Post> {
        if (!post.id.equals(this.post.value?.id, true)) {
            this.post.value = Post("")
            ref.document(post.id).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> this.post.value = snapshot.post
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return this.post
    }

    fun getFavorites(): LiveData<List<Post>> {
        if (favoritesList.isEmpty()) {
            auth = FirebaseAuth.getInstance()
            auth.currentUser?.let { user ->
                ref.whereEqualTo("favorite_for.${user.uid}", true)
                    .addSnapshotListener { snapshot, exception ->
                        when {
                            exception != null -> onListenFailed(tag, exception)
                            snapshot != null -> {
                                favoritesList = mutableListOf()
                                favorites.value = favoritesList.apply {
                                    for (doc in snapshot)
                                        add(doc.post)
                                    sortByDescending { it.timestamp }
                                }
                            }
                            else -> onSnapshotNull(tag)
                        }
                    }
            }
        }
        return favorites
    }

    fun addToFavorites(post: Post, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            val data = mapOf(
                "favorite_for" to mapOf(
                    user.uid to true
                )
            )
            ref.document(post.id).set(data, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful)
                    onSuccessful()
            }
        }
    }

    fun removeFromFavorites(post: Post, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            val data = mapOf(
                "favorite_for" to mapOf(
                    user.uid to null
                )
            )
            ref.document(post.id).set(data, SetOptions.merge()).addOnCompleteListener {
                if (it.isSuccessful)
                    onSuccessful()
            }
        }
    }

    fun isThisMyFavorite(post: Post): LiveData<Boolean> {
        isThisMyFavorite.value = false
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            ref.whereEqualTo("favorite_for.${it.uid}", true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            for (doc in snapshot)
                                if (doc.post.id.equals(post.id, false)) {
                                    isThisMyFavorite.value = true
                                    break
                                } else
                                    isThisMyFavorite.value = false
                        }
                        else -> onSnapshotNull(tag)
                    }
                }

        }
        return isThisMyFavorite
    }

}
