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

package io.github.horaciocome1.reeque.data.user

import io.github.horaciocome1.reeque.data.topic.Topic

class UserRepository private constructor(private val userDAO: UserDAO) {

    fun addUser(user: User) = userDAO.addUser(user)

    fun getUsers(topic: Topic) = userDAO.getUsers(topic)

    fun setUser(user: User) = userDAO.setUser(user)

    fun getUser(key: String) = userDAO.getUser(key)

    companion object {
        @Volatile private var instance: UserRepository? = null
        fun getInstance(userDAO: UserDAO) = instance ?: synchronized(this) {
            instance ?: UserRepository(userDAO).also { instance = it }
        }
    }

}