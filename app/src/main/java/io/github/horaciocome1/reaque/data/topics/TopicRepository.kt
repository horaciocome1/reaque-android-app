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

package io.github.horaciocome1.reaque.data.topics

class TopicRepository private constructor(private val topicWebService: TopicWebService) {

    fun addTopic(topic: Topic) = topicWebService.addTopic(topic)

    fun getTopics() = topicWebService.getTopics()

    companion object {
        @Volatile private var instance: TopicRepository? = null
        fun getInstance(topicWebService: TopicWebService) = instance ?: synchronized(this) {
            instance ?: TopicRepository(topicWebService).also { instance = it }
        }
    }

}