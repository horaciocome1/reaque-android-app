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
import io.github.horaciocome1.reaque.data.topics.Topic

class UsersRepository private constructor(private val webService: UsersWebService) {

    fun addUser(onSuccessful: () -> Unit) = webService.addUser(onSuccessful)

    fun addTopicToUser(topic: Topic, onSuccessful: () -> Unit) = webService.addTopicToUser(topic, onSuccessful)

    fun getUsers(topic: Topic) = webService.getUsers(topic)

    fun getUsers(user: User) = webService.getUsers(user)

    val me = webService.me as LiveData<User>

    val favorites = webService.getFavorites()

    companion object {
        @Volatile
        private var instance: UsersRepository? = null

        fun getInstance(usersWebService: UsersWebService) = instance ?: synchronized(this) {
            instance ?: UsersRepository(usersWebService).also { instance = it }
        }
    }

}