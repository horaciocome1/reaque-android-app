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

package io.github.horaciocome1.reaque.data.comments

import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic

class CommentsRepository private constructor(private val commentsWebService: CommentsWebService) {

    fun getComments(topic: Topic) = commentsWebService.getComments(topic)

    fun getComments(post: Post) = commentsWebService.getComments(post)

    companion object {

        @Volatile
        private var instance: CommentsRepository? = null

        fun getInstance(commentsWebService: CommentsWebService) = instance ?: synchronized(this) {
            instance ?: CommentsRepository(commentsWebService).also { instance = it }
        }

    }

}