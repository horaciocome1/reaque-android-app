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
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class UsersWebService {

    private val tag = "UsersWebService"

    /*list of users that belong to the same notEmptyTopics*/
    private var topicUsersList = mutableListOf<User>()
    private val topicUsers = MutableLiveData<List<User>>()

    private val _user = User("")
    private val user = MutableLiveData<User>()

    private val topic = Topic("")

    private val ref = Firebase.firestore.collection("users")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val me = MutableLiveData<User>()
        get() {
            auth.addSimpleAuthStateListener {
                ref.document(it.uid).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> field.value = snapshot.user
                        else -> onSnapshotNull(tag)
                    }
                }
            }
            return field
        }

    private var favoritesList = mutableListOf<User>()
    val favorites = MutableLiveData<List<User>>()
        get() {
            auth.addAuthStateListener {
                if (favoritesList.isEmpty())
                    ref.whereEqualTo("favorite_for.${it.uid}", true).addSnapshotListener { snapshot, exception ->
                        when {
                            exception != null -> onListenFailed(tag, exception)
                            snapshot != null -> {
                                favoritesList = snapshot.users
                                field.value = favoritesList
                            }
                            else -> onSnapshotNull(tag)
                        }
                    }
            }
            return field
        }

    val isThisMyFavorite = MutableLiveData<Boolean>()
        get() {
            field.value = false
            auth.addSimpleAuthStateListener {
                ref.document(_user.id).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> isThisMyFavorite.value =
                            snapshot["favorite_for.${it.uid}"].toString().toBoolean()
                        else -> onSnapshotNull(tag)
                    }
                }
            }
            return field
        }

    fun submitProfileUpdates(user: User, onSuccessful: () -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document(it.uid).set(user.map, SetOptions.merge()).addOnSuccessListener {
                onAddUserSucceed(tag)
                onSuccessful()
            }.addOnFailureListener { exception ->
                onAddUserFailed(tag, exception)
            }
        }
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        auth.addSimpleAuthStateListener {
            if (!this.topic.id.equals(topic.id, true)) {
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
                this.topic.id = topic.id
            }
        }
        return topicUsers
    }

    fun getUsers(user: User): LiveData<User> {
        auth.addSimpleAuthStateListener {
            if (!user.id.equals(_user.id, true)) {
                this.user.value = user // empty fields to clean
                ref.document(user.id).addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> this.user.value = snapshot.user
                        else -> onSnapshotNull(tag)
                    }
                }
                _user.id = user.id
            }
        }
        return this.user
    }

}