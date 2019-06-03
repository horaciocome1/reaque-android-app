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
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class UsersWebService {

    private val tag = "UsersWebService"

    /*list of users that belong to the same notEmptyTopicsForPosts*/
    private var topicUsersList = mutableListOf<User>()
    private val topicUsers = MutableLiveData<List<User>>()

    private val _user = User("")
    private val user = MutableLiveData<User>()

    private val topic = Topic("")

    private val ref = FirebaseFirestore.getInstance().collection("users")

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val me = MutableLiveData<User>()
        get() {
            auth.addSimpleAuthStateListener { user ->
                _user.id = user.uid
                ref.document(user.uid).addSimpleSnapshotListener(tag) {
                    field.value = it.user
                }
            }
            return field
        }

    private var favoritesList = mutableListOf<User>()
    val favorites = MutableLiveData<List<User>>()
        get() {
            auth.addAuthStateListener { user ->
                if (favoritesList.isEmpty())
                    ref.whereEqualTo("favorite_for.${user.uid}", true).addSimpleSnapshotListener(tag) {
                        favoritesList = it.users
                        favorites.value = it.users
                    }
            }
            return field
        }

    private val isThisMyFavorite = MutableLiveData<Boolean>()

    fun isThisMyFavorite(user: User): LiveData<Boolean> {
        isThisMyFavorite.value = false
        auth.addSimpleAuthStateListener { u ->
            ref.document(user.id).addSimpleSnapshotListener(tag) {
                isThisMyFavorite.value = it["favorite_for.${u.uid}"].toString().toBoolean()
            }
        }
        return isThisMyFavorite
    }

    fun submitProfileUpdates(user: User, onSuccessful: () -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document(it.uid).set(user.map, SetOptions.merge())
                .addOnSuccessListener {
                    onAddUserSucceed(tag)
                    onSuccessful()
                }
                .addOnFailureListener { exception ->
                    onAddUserFailed(tag, exception)
                }
        }
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        if (!this.topic.id.equals(topic.id, true)) {
            topicUsers.value = mutableListOf()
            auth.addSimpleAuthStateListener {
                ref.whereEqualTo("topics.${topic.id}", true).addSimpleSnapshotListener(tag) {
                    topicUsersList = it.users
                    topicUsers.value = it.users
                }
                this.topic.id = topic.id
            }
        }
        return topicUsers
    }

    fun getUsers(user: User): LiveData<User> {
        if (!user.id.equals(_user.id, true)) {
            this.user.value = User("")
            auth.addSimpleAuthStateListener {
                ref.document(user.id).addSimpleSnapshotListener(tag) {
                    _user.id = it.user.id
                    this.user.value = it.user
                }
            }
        }
        return this.user
    }

}