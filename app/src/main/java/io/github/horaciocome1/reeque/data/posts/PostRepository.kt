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

package io.github.horaciocome1.reeque.data.posts

import io.github.horaciocome1.reeque.data.topics.Topic
import io.github.horaciocome1.reeque.data.users.User

class PostRepository private constructor(private val postDAO: PostDAO) {

    fun addPost(post: Post) = postDAO.addPost(post)

    fun getPosts(topic: Topic) = postDAO.getPosts(topic)

    fun getPosts(key: String) = postDAO.getPosts(key)

    fun getPosts(topic: Topic, user: User) = postDAO.getPosts(topic, user)

    companion object {
        @Volatile private var instance: PostRepository? = null
        fun getInstance(postDAO: PostDAO) = instance ?: synchronized(this) {
            instance ?: PostRepository(postDAO).also { instance = it }
        }
    }

}