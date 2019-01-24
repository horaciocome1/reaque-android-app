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

package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull
import io.github.horaciocome1.reaque.utilities.toUser

class UserWebService {

    private val tag = "UserWebService"
    private val myId = "IojGZNYQm40Zl7Xe59lx"

    /*list of users that belong to the same topic*/
    private var topicUsersList = mutableListOf<User>()
    private val topicUsers = MutableLiveData<List<User>>()

    /*
    * not a list a all
    * only used the firt position filled by the writer that the user is currently viewing its profile
    */
    private var viewingUsersList = mutableListOf(User(""))
    private val viewingUsers = MutableLiveData<List<User>>()

    /*
    * not a list a all
    * only used the firt position filled by the data of app user
    */
    private var usersList = mutableListOf<User>()
    private val users = MutableLiveData<List<User>>()

    private val topic = Topic("")

    private val reference = FirebaseFirestore.getInstance().collection("users")

    fun addUser(user: User) {
        usersList.add(user)
        users.value = usersList
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        if (!this.topic.id.equals(topic.id, true)) {
            topicUsers.value = mutableListOf()
            reference.whereEqualTo(topic.id, true).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        topicUsersList = mutableListOf()
                        for (doc in snapshot.documents)
                            topicUsersList.add(doc.toUser())
                        topicUsers.value = topicUsersList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
            this.topic.id = topic.id
        }
        return topicUsers
    }

    fun getUsers(user: User): LiveData<List<User>> {
        if (!user.id.equals(viewingUsersList[0].id, true)) {
            viewingUsers.value = mutableListOf()
            reference.document(user.id).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        viewingUsersList = mutableListOf(snapshot.toUser())
                        viewingUsers.value = viewingUsersList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return viewingUsers
    }

    fun getUsers(): LiveData<List<User>> {
        if (usersList.isEmpty()) {
            reference.document(myId).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        usersList = mutableListOf(snapshot.toUser())
                        users.value = usersList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return users
    }

}