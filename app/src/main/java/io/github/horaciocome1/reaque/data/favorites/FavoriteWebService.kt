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

package io.github.horaciocome1.reaque.data.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull

class FavoriteWebService {

    private val tag = "FavoriteWebService"
    private val myId = "FRWsZTrrI0PTp1Fqftdb"

    private var topicsList = mutableListOf<Topic>()
    private val topics = MutableLiveData<List<Topic>>()

    private var postsList = mutableListOf<Post>()
    private val posts = MutableLiveData<List<Post>>()

    private val postsReference = FirebaseFirestore.getInstance().collection("user_favorite_posts")
    private val topicsReference = FirebaseFirestore.getInstance().collection("user_favorite_topics")

    fun getTopics(): LiveData<List<Topic>> {
        if (topicsList.isEmpty())
            topicsReference.whereEqualTo(myId, true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            for (doc in snapshot.documents)
                                postsList.add(Post(doc.id).apply { title = doc["title"].toString() })
                            posts.value = postsList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
        return topics
    }

    fun getPosts(): LiveData<List<Post>> {
        if (postsList.isEmpty())
            postsReference.whereEqualTo(myId, true)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            for (doc in snapshot.documents)
                                topicsList.add(Topic(doc.id).apply { title = doc["title"].toString() })
                            topics.value = topicsList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
        return posts
    }

}