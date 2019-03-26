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

package io.github.horaciocome1.reaque.data

import io.github.horaciocome1.reaque.data.comments.CommentsWebService
import io.github.horaciocome1.reaque.data.media.ImageUploaderWebService
import io.github.horaciocome1.reaque.data.notifications.NotificationsWebService
import io.github.horaciocome1.reaque.data.posts.PostsWebService
import io.github.horaciocome1.reaque.data.topics.TopicsWebService
import io.github.horaciocome1.reaque.data.users.UsersWebService

class Database private constructor() {

    var topicsWebService = TopicsWebService()
    var usersWebService = UsersWebService()
    var postsWebService = PostsWebService()
    var commentsWebService = CommentsWebService()
    var notificationsWebService = NotificationsWebService()
    var imageUploaderWebService = ImageUploaderWebService()

    companion object {
        @Volatile private var instance: Database? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Database().also { instance = it }
        }
    }

}