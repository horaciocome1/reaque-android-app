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

class TopicsRepository private constructor(
    service: TopicsService
) {

    val notEmptyTopics: LiveData<List<Topic>> by lazy {
        service.notEmptyTopics
    }

    val topics: LiveData<List<Topic>> by lazy {
        service.topics
    }

    companion object {

        @Volatile
        private var instance: TopicsRepository? = null

        fun getInstance(topicsService: TopicsService) = instance ?: synchronized(this) {
            instance ?: TopicsRepository(topicsService)
                .also {
                    instance = it
                }
        }

    }

}