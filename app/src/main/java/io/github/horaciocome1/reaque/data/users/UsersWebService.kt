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
import io.github.horaciocome1.reaque.data._topics.Topic
import io.github.horaciocome1.reaque.data._users.User
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
            auth.addSimpleAuthStateListener { firebaseUser ->
                ref.document(firebaseUser.uid).addSimpleSnapshotListener(tag) {
                    field.value = it.user
                }
            }
            return field
        }

    private var favoritesList = mutableListOf<User>()
    val favorites = MutableLiveData<List<User>>()
        get() {
            if (favoritesList.isEmpty())
                auth.addSimpleAuthStateListener { user ->
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
        ref.document(user.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, firebaseUser ->
            isThisMyFavorite.value = snapshot["favorite_for.${firebaseUser.uid}"].toString().toBoolean()
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
            ref.whereEqualTo("topics.${topic.id}", true).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                topicUsersList = snapshot.users
                topicUsers.value = snapshot.users
            }
            this.topic.id = topic.id
        }
        return topicUsers
    }

    fun getUsers(user: User): LiveData<User> {
        if (!user.id.equals(_user.id, true)) {
            this.user.value = User("")
            ref.document(user.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                _user.id = snapshot.user.id
                this.user.value = snapshot.user
            }
        }
        return this.user
    }

}