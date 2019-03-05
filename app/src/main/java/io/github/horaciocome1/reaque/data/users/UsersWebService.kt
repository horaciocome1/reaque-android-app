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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull
import io.github.horaciocome1.reaque.utilities.user

class UsersWebService {

    private val tag = "UsersWebService"
    private val myId = "FRWsZTrrI0PTp1Fqftdb"

    /*list of users that belong to the same topics*/
    private var topicUsersList = mutableListOf<User>()
    private val topicUsers = MutableLiveData<List<User>>()

    private val user = MutableLiveData<User>().apply {
        value = User("")
    }

    val me = MutableLiveData<User>().apply {
        value = User("")
    }

    private var favoritesList = mutableListOf<User>()
    private val favorites = MutableLiveData<List<User>>()

    private var topicId = ""

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("users")
    private val favoritesRef = db.collection("favorites")

    init {
        ref.document(myId).addSnapshotListener { snapshot, exception ->
            when {
                exception != null -> onListenFailed(tag, exception)
                snapshot != null -> me.value = snapshot.user
                else -> onSnapshotNull(tag)
            }
        }
    }

    fun addUser(user: User) {
//        usersList.add(user)
//        users.value = usersList
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        if (!topicId.equals(topic.id, true)) {
            topicUsers.value = mutableListOf()
            ref.whereEqualTo(topic.id, true)
                .orderBy("date", Query.Direction.DESCENDING)
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
        if (favoritesList.isEmpty())
            favoritesRef.whereEqualTo("user", true)
                .whereEqualTo(myId, true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
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