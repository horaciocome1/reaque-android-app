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

package io.github.horaciocome1.reeque.data

import io.github.horaciocome1.reeque.data.favorites.FavoriteDAO
import io.github.horaciocome1.reeque.data.posts.PostDAO
import io.github.horaciocome1.reeque.data.topics.TopicDAO
import io.github.horaciocome1.reeque.data.users.UserDAO

class Database private constructor() {

    var topicDAO = TopicDAO()
    var userDAO = UserDAO()
    var postDAO = PostDAO()
    var favoriteDAO = FavoriteDAO()

    companion object {
        @Volatile private var instance: Database? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Database().also { instance = it }
        }
    }

}