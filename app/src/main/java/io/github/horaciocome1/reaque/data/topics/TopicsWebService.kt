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

package io.github.horaciocome1.reaque.data.topics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull
import io.github.horaciocome1.reaque.utilities.topic

class TopicsWebService {

    private val tag = "TopicsWebService"
    private val myId = "FRWsZTrrI0PTp1Fqftdb"

    private var topicsList = mutableListOf<Topic>()
    val topics = MutableLiveData<List<Topic>>()

    private var favoritesList = mutableListOf<Topic>()
    private val favorites = MutableLiveData<List<Topic>>()

    private val db = FirebaseFirestore.getInstance()
    private val ref = db.collection("topics")
    private val favoritesRef = db.collection("favorites")

    init {
        ref.addSnapshotListener { snapshot, exception ->
            when {
                exception != null -> onListenFailed(tag, exception)
                snapshot != null -> {
                    for (doc in snapshot.documents)
                        topicsList.add(doc.topic)
                    topics.value = topicsList
                }
                else -> onSnapshotNull(tag)
            }
        }
    }

    fun getFavorites(): LiveData<List<Topic>> {
        if (favoritesList.isEmpty()) {
            favoritesRef.whereEqualTo("topic", true)
                .whereEqualTo(myId, true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            for (doc in snapshot.documents)
                                favoritesList.add(doc.topic)
                            favorites.value = favoritesList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
        }
        return favorites
    }

    fun addTopic(topic: Topic) {
        topicsList.add(topic)
        topics.value = topicsList
    }

}