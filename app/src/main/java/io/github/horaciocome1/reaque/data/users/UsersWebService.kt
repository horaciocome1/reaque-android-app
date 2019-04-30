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

package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class UsersWebService {

    private val tag = "UsersWebService"

    /*list of users that belong to the same topics*/
    private var topicUsersList = mutableListOf<User>()
    private val topicUsers = MutableLiveData<List<User>>()

    private val user = MutableLiveData<User>().apply {
        value = User("")
    }

    val me = MutableLiveData<User>()

    private var favoritesList = mutableListOf<User>()
    private val favorites = MutableLiveData<List<User>>()

    private val isThisMyFavorite = MutableLiveData<Boolean>().apply { value = true }

    private var topicId = ""

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("users")

    private var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            ref.document(it.uid)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> me.value = snapshot.user
                        else -> onSnapshotNull(tag)
                    }
                }
        }
    }

    fun addUser(onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            ref.document(it.uid).set(it.map, SetOptions.merge()).addOnSuccessListener {
                onAddUserSucceed(tag)
                onSuccessful()
            }.addOnFailureListener { exception ->
                onAddUserFailed(tag, exception)
            }
        }
    }

    fun editUser(user: User, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            ref.document(it.uid).set(user.map, SetOptions.merge()).addOnSuccessListener {
                onAddUserSucceed(tag)
                onSuccessful()
            }.addOnFailureListener { exception ->
                onAddUserFailed(tag, exception)
            }
        }
    }

    fun addTopicToUser(topic: Topic, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            val data = mapOf(
                "topics" to mapOf(
                    topic.id to true
                )
            )
            ref.document(it.uid).set(data, SetOptions.merge()).addOnSuccessListener {
                onAddUserSucceed(tag)
                onSuccessful()
            }.addOnFailureListener { exception ->
                onAddUserFailed(tag, exception)
            }
        }
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        if (!topicId.equals(topic.id, true)) {
            topicUsers.value = mutableListOf()
            ref.whereEqualTo("topics.${topic.id}", true).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            topicUsersList = snapshot.users
                            topicUsers.value = topicUsersList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
            topicId = topic.id
        }
        return topicUsers
    }

    fun getUsers(user: User): LiveData<User> {
        if (!user.id.equals(this.user.value?.id, true)) {
            this.user.value = User("")
            ref.document(user.id).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> this.user.value = snapshot.user
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return this.user
    }


    fun getFavorites(): LiveData<List<User>> {
        if (favoritesList.isEmpty()) {
            auth = FirebaseAuth.getInstance()
            auth.currentUser?.let { firebaseUser ->
                ref.whereEqualTo("favorite_for.${firebaseUser.uid}", true)
                    .addSnapshotListener { snapshot, exception ->
                        when {
                            exception != null -> onListenFailed(tag, exception)
                            snapshot != null -> {
                                favoritesList = snapshot.users
                                favorites.value = favoritesList
                            }
                            else -> onSnapshotNull(tag)
                        }
                    }
            }
        }
        return favorites
    }

    // add id from the post i like to my profile
    fun addToFavorites(post: Post, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            val data = mapOf(
                "favorites" to mapOf(
                    post.id to true
                )
            )
            ref.document(user.uid).set(data, SetOptions.merge()).addOnSuccessListener {
                onSuccessful()
            }
        }
    }

    // remove id from the post i like from my profile
    fun removeFromFavorites(post: Post, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            val data = mapOf(
                "favorites" to mapOf(
                    post.id to null
                )
            )
            ref.document(user.uid).set(data, SetOptions.merge()).addOnSuccessListener {
                onSuccessful()
            }
        }
    }

    fun addToFavorites(user: User, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { me ->
            // add the id from the user i like to my profile
            var data = mapOf(
                "favorites" to mapOf(
                    user.id to true
                )
            )
            ref.document(me.uid).set(data, SetOptions.merge()).addOnSuccessListener {
                // add my id to the profile of the user i like
                data = mapOf(
                    "favorite_for" to mapOf(
                        me.uid to true
                    )
                )
                ref.document(user.id).set(data, SetOptions.merge()).addOnSuccessListener { task ->
                    onSuccessful()
                }
            }
        }
    }

    fun removeFromFavorites(user: User, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { me ->
            // add the id from the user i like to my profile
            var data = mapOf(
                "favorites" to mapOf(
                    user.id to null
                )
            )
            ref.document(me.uid).set(data, SetOptions.merge()).addOnSuccessListener {
                // add my id to the profile of the user i like
                data = mapOf(
                    "favorite_for" to mapOf(
                        me.uid to null
                    )
                )
                ref.document(user.id).set(data, SetOptions.merge()).addOnSuccessListener {
                    onSuccessful()
                }
            }
        }
    }

    fun isThisFavoriteForMe(user: User): LiveData<Boolean> {
        isThisMyFavorite.value = false
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let {
            ref.document(it.uid).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> isThisMyFavorite.value = snapshot["favorites.${user.id}"].toString().toBoolean()
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return isThisMyFavorite
    }

}