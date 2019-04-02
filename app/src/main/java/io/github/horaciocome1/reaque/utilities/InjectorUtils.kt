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

package io.github.horaciocome1.reaque.utilities

import io.github.horaciocome1.reaque.data.Database
import io.github.horaciocome1.reaque.data.comments.CommentsRepository
import io.github.horaciocome1.reaque.data.media.ImageRepository
import io.github.horaciocome1.reaque.data.notifications.NotificationsRepository
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.ui.forum.ForumViewModelFactory
import io.github.horaciocome1.reaque.ui.more.MoreViewModelFactory
import io.github.horaciocome1.reaque.ui.notifications.NotificationsViewModelFactory
import io.github.horaciocome1.reaque.ui.posts.PostsViewModelFactory
import io.github.horaciocome1.reaque.ui.users.UsersViewModelFactory

object InjectorUtils {

    val usersViewModelFactory: UsersViewModelFactory
        get() {
            val usersRepository = UsersRepository.getInstance(Database.getInstance().usersWebService)
            val topicsRepository = TopicsRepository.getInstance(Database.getInstance().topicsWebService)
            val postsRepository = PostsRepository.getInstance(Database.getInstance().postsWebService)
            return UsersViewModelFactory(usersRepository, topicsRepository, postsRepository)
        }

    val moreViewModelFactory: MoreViewModelFactory
        get() {
            val repository = UsersRepository.getInstance(Database.getInstance().usersWebService)
            return MoreViewModelFactory(repository)
        }

    val postsViewModelFactory: PostsViewModelFactory
        get() {
            val topicsRepository = TopicsRepository.getInstance(Database.getInstance().topicsWebService)
            val postsRepository = PostsRepository.getInstance(Database.getInstance().postsWebService)
            val imageRepository = ImageRepository.getInstance(Database.getInstance().imageUploaderWebService)
            return PostsViewModelFactory(postsRepository, topicsRepository, imageRepository)
        }

    val forumViewModelFactory: ForumViewModelFactory
        get() {
            val topicsRepository = TopicsRepository.getInstance(Database.getInstance().topicsWebService)
            val commentsRepository = CommentsRepository.getInstance(Database.getInstance().commentsWebService)
            return ForumViewModelFactory(commentsRepository, topicsRepository)
        }

    val notificationsViewModelFactory: NotificationsViewModelFactory
        get() {
            val repository = NotificationsRepository.getInstance(Database.getInstance().notificationsWebService)
            return NotificationsViewModelFactory(repository)
        }

}