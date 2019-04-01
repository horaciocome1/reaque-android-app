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

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.hashMap
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull
import io.github.horaciocome1.reaque.utilities.user

class UsersWebService {

    private val tag = "UsersWebService"

    /*list of users that belong to the same topics*/
    private var topicUsersList = mutableListOf<User>()
    private val topicUsers = MutableLiveData<List<User>>()

    private val user = MutableLiveData<User>().apply {
        value = User("")
    }

    val me = MutableLiveData<User>().apply {
        value = User("")
    }
        get() {
            auth = FirebaseAuth.getInstance()
            ref.document(auth.currentUser?.uid.toString())
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> field.value = snapshot.user
                        else -> onSnapshotNull(tag)
                    }
                }
            return field
        }

    private var favoritesList = mutableListOf<User>()
    private val favorites = MutableLiveData<List<User>>()

    private var topicId = ""

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("users")

    private lateinit var auth: FirebaseAuth

    fun addUser(onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        auth.currentUser?.let { user ->
            ref.document(user.uid)
                .set(user.hashMap, SetOptions.merge())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(tag, "User successfully written!")
                        onSuccessful()
                    } else
                        Log.w(tag, "Error writing document", it.exception)
                }
        }
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        if (!topicId.equals(topic.id, true)) {
            topicUsers.value = mutableListOf()
            ref.whereEqualTo("topics.${topic.id}", true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            topicUsersList = mutableListOf()
                            for (doc in snapshot.documents)
                                topicUsersList.add(doc.user)
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
        auth = FirebaseAuth.getInstance()
        if (favoritesList.isEmpty())
            ref.whereEqualTo("favorite_users.${auth.currentUser?.uid.toString()}", true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            favoritesList = mutableListOf()
                            for (doc in snapshot.documents)
                                favoritesList.add(doc.user)
                            favorites.value = favoritesList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
        return favorites
    }

}