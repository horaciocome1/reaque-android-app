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

package io.github.horaciocome1.reeque.data.topics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reeque.data.users.User
import io.github.horaciocome1.reeque.utilities.onListenFailed
import io.github.horaciocome1.reeque.utilities.onSnapshotNull
import io.github.horaciocome1.reeque.utilities.toTopic

class TopicDAO {

    private val tag = "TopicDAO"

    private var topicList = mutableListOf<Topic>()
    private val topics = MutableLiveData<List<Topic>>()

    private var userTopicList = mutableListOf<Topic>()
    private val userTopics = MutableLiveData<List<Topic>>()

    private val reference = FirebaseFirestore.getInstance().collection("topics")

    fun addTopic(topic: Topic) {
        topicList.add(topic)
        topics.value = topicList
    }

    fun getTopics(): LiveData<List<Topic>> {
        if (topicList.isEmpty())
            reference.addSnapshotListener {
                snapshot, exception-> when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        for (doc in snapshot.documents)
                            topicList.add(doc.toTopic())
                        topics.value = topicList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
        return topics
    }

//        fun getTopics(user: User) = topics as LiveData<List<Topic>>
    fun getTopics(user: User): LiveData<List<Topic>> {
        if (userTopicList.isEmpty()) {
//            for (id in user.topicsId)
                reference.addSnapshotListener {
                    snapshot, exception -> when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null ->
                            for (doc in snapshot.documents)
//                                if (doc.id in user.topicsId)
                                    userTopicList.add(doc.toTopic())
                        else -> onSnapshotNull(tag)
                    }
                }
            userTopics.value = userTopicList
        }
        return userTopics
    }

}