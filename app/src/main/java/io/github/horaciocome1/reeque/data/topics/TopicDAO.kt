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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.posts.Post
import io.github.horaciocome1.reeque.data.users.User

class TopicDAO {

    private val topicList = mutableListOf<Topic>()
    private val topics = MutableLiveData<List<Topic>>()

    private val db = FirebaseFirestore.getInstance()

    init {
        topics().forEach { topicList += it }
        topics().forEach { topicList += it }
        topics().forEach { topicList += it }
        topics().forEach { topicList += it }
        topics().forEach { topicList += it }
        topics().forEach { topicList += it }
        topics().forEach { topicList += it }


        topics.value = topicList
    }

    fun addTopic(topic: Topic) {
        topicList.add(topic)
        topics.value = topicList
    }

    //    fun getTopics() = topics as LiveData<List<Topic>>
    fun getTopics() {
        if (topicList.isEmpty()) {

        }
    }

    fun getTopics(user: User) = topics as LiveData<List<Topic>>

    private fun topics() = arrayListOf(
        Topic("Casamento").apply {
            totalPosts = 132
            totalReaders = 724

            posts.add(Post("Sem Querer Foi Acontecer de Novo?").apply {
                rating = 4.1f
                user = User("").apply { pic = R.drawable.profile3 }
            })
            posts.add(Post("Casados de Fresco").apply {
                rating = 4.0f
                user = User("").apply { pic = R.drawable.profile2 }
            })
            posts.add(Post("Vida a Dois ++").apply {
                rating = 3.7f
                user = User("").apply { pic = R.drawable.profile3 }
            })
        },
        Topic("Cultura").apply {
            totalPosts = 9
            totalReaders = 3428

            posts.add(Post("Ndau").apply {
                rating = 4.8f
                user = User("").apply { pic = R.drawable.profile2 }
            })
            posts.add(Post("Carringana Wa Carringana").apply {
                rating = 4.6f
                user = User("").apply { pic = R.drawable.profile3 }
            })
            posts.add(Post("Ximitso Hi Kwatsi").apply {
                rating = 3.9f
                user = User("").apply { pic = R.drawable.profile2 }
            })
        },
        Topic("Lugares").apply {
            totalPosts = 132
            totalReaders = 724

            posts.add(Post("O Monte Mais Alto - O Mais Solitario").apply {
                rating = 4.1f
                user = User("").apply { pic = R.drawable.profile3 }
            })
            posts.add(Post("Nascer Do Sol Como Mais Nenhum").apply {
                rating = 4.0f
                user = User("").apply { pic = R.drawable.profile2 }
            })
            posts.add(Post("O Canto Molhado Mais A Norte").apply {
                rating = 3.7f
                user = User("").apply { pic = R.drawable.profile3 }
            })
        }
    )

}