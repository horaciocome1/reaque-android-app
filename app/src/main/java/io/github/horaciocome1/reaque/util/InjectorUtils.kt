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

package io.github.horaciocome1.reaque.util

import io.github.horaciocome1.reaque.data.Database
import io.github.horaciocome1.reaque.data._storage.StorageRepository
import io.github.horaciocome1.reaque.data._topics.TopicsRepository
import io.github.horaciocome1.reaque.ui._more.MoreViewModelFactory
import io.github.horaciocome1.reaque.ui.notifications.NotificationsViewModelFactory
import io.github.horaciocome1.reaque.ui.posts.PostsViewModelFactory
import io.github.horaciocome1.reaque.ui.users.UsersViewModelFactory

object InjectorUtils {

    val usersViewModelFactory: UsersViewModelFactory
        get() {
            val usersRepository = UsersRepository.getInstance(Database.getInstance().usersWebService)
            val topicsRepository = TopicsRepository.getInstance(Database.getInstance().topicsWebService)
            val postsRepository = PostsRepository.getInstance(Database.getInstance().postsWebService)
            val favoritesRepository = FavoritesRepository.getInstance(Database.getInstance().favoritesWebService)
            return UsersViewModelFactory(usersRepository, topicsRepository, postsRepository, favoritesRepository)
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
            val imageRepository = StorageRepository.getInstance(Database.getInstance().imageUploaderWebService)
            val usersRepository = UsersRepository.getInstance(Database.getInstance().usersWebService)
            val favoritesRepository = FavoritesRepository.getInstance(Database.getInstance().favoritesWebService)
            return PostsViewModelFactory(
                postsRepository,
                topicsRepository,
                imageRepository,
                usersRepository,
                favoritesRepository
            )
        }

    val notificationsViewModelFactory: NotificationsViewModelFactory
        get() {
            val repository = NotificationsRepository.getInstance(Database.getInstance().notificationsWebService)
            return NotificationsViewModelFactory(repository)
        }

}