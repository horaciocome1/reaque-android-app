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
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic

class UsersRepository private constructor(private val service: UsersWebService) {

    fun addUser(onSuccessful: () -> Unit) = service.addUser(onSuccessful)

    fun editUser(user: User, onSuccessful: () -> Unit) = service.editUser(user, onSuccessful)

    fun addTopicToUser(topic: Topic, onSuccessful: () -> Unit) = service.addTopicToUser(topic, onSuccessful)

    fun addToFavorites(post: Post, onSuccessful: () -> Unit) = service.addToFavorites(post, onSuccessful)

    fun removeFromFavorites(post: Post, onSuccessful: () -> Unit) = service.removeFromFavorites(post, onSuccessful)

    fun addToFavorites(user: User, onSuccessful: () -> Unit) = service.addToFavorites(user, onSuccessful)

    fun removeFromFavorites(user: User, onSuccessful: () -> Unit) = service.removeFromFavorites(user, onSuccessful)

    fun isThisFavoriteForMe(user: User) = service.isThisFavoriteForMe(user)

    fun getUsers(topic: Topic) = service.getUsers(topic)

    fun getUsers(user: User) = service.getUsers(user)

    val me = service.me as LiveData<User>

    val favorites = service.getFavorites()

    companion object {
        @Volatile
        private var instance: UsersRepository? = null

        fun getInstance(usersWebService: UsersWebService) = instance ?: synchronized(this) {
            instance ?: UsersRepository(usersWebService).also { instance = it }
        }

    }

}