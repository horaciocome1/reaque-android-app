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

package io.github.horaciocome1.reeque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reeque.data.topics.Topic
import io.github.horaciocome1.reeque.utilities.onListenFailed
import io.github.horaciocome1.reeque.utilities.onSnapshotNull
import io.github.horaciocome1.reeque.utilities.toTopicId
import io.github.horaciocome1.reeque.utilities.toUser

class UserDAO {

    private val tag = "UserDAO"

    private var userList = mutableListOf<User>()
    private val users = MutableLiveData<List<User>>()

    private val user = MutableLiveData<User>()

    private val topicId = MutableLiveData<List<String>>()

    private val topic = Topic("")

    private val reference = FirebaseFirestore.getInstance().collection("users")

    fun addUser(user: User) {
        userList.add(user)
        users.value = userList
    }

    fun getUsers(topic: Topic): LiveData<List<User>> {
        if (userList.isEmpty() or !this.topic.id.equals(topic.id, true)) {
            reference.whereEqualTo(topic.id, true).addSnapshotListener {
                snapshot, exception -> when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        userList = mutableListOf()
                        for (doc in snapshot.documents)
                            userList.add(doc.toUser())
                        users.value = userList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
            this.topic.id = topic.id
        }
        return users
    }

    fun getUsers(user: User): LiveData<User> {
        reference.document(user.id).addSnapshotListener {
            snapshot, exception -> when {
                exception != null -> onListenFailed(tag, exception)
                snapshot != null -> this.user.value = snapshot.toUser()
                else -> onSnapshotNull(tag)
            }
        }
        return this.user
    }

    fun getTopicId(user: User): LiveData<List<String>> {
        reference.document(user.id).addSnapshotListener {
            snapshot, exception -> when {
                exception != null -> onListenFailed(tag, exception)
                snapshot != null -> this.topicId.value = snapshot.toTopicId()
                else -> onSnapshotNull(tag)
            }
        }
        return topicId
    }

}