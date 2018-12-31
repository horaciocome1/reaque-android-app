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
import io.github.horaciocome1.reeque.R

class TopicDAO {

    private val topicList = mutableListOf<Topic>()
    private val topics = MutableLiveData<List<Topic>>()

    init {
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

    fun getTopics() = topics as LiveData<List<Topic>>

    private fun topics() = arrayListOf(
        Topic("Casamento").apply {
            totalPosts = 132
            totalReaders = 724

            post1Rating = 4.1f
            post1Title = "Sem Querer Foi Acontecer de Novo?"
            post1ProfilePic = R.drawable.profile3

            post2Rating = 4.0f
            post2Title = "Casados de Fresco"
            post2ProfilePic = R.drawable.profile2

            post3Rating = 3.7f
            post3Title = "Vida a Dois ++"
            post3ProfilePic = R.drawable.profile3
        },
        Topic("Cultura").apply {
            totalPosts = 9
            totalReaders = 3428

            post1Rating = 4.8f
            post1Title = "Ndau"
            post1ProfilePic = R.drawable.profile2

            post2Rating = 4.5f
            post2Title = "Carringana Wa Carringana"
            post2ProfilePic = R.drawable.profile3

            post3Rating = 4.0f
            post3Title = "Ximitso Hi Kwatsi"
            post3ProfilePic = R.drawable.profile3
        },
        Topic("Lugares").apply {
            totalPosts = 132
            totalReaders = 724

            post1Rating = 4.1f
            post1Title = "Monte Binga"
            post1ProfilePic = R.drawable.profile2

            post2Rating = 4.0f
            post2Title = "Baia dos Cocos"
            post2ProfilePic = R.drawable.profile2

            post3Rating = 3.7f
            post3Title = "Lago Niassa"
            post3ProfilePic = R.drawable.profile3
        }
    )

}