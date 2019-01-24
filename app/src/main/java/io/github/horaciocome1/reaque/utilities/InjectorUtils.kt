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

package io.github.horaciocome1.reaque.utilities

import io.github.horaciocome1.reaque.data.Database
import io.github.horaciocome1.reaque.data.comments.CommentsRepository
import io.github.horaciocome1.reaque.data.favorites.FavoriteRepository
import io.github.horaciocome1.reaque.data.posts.PostRepository
import io.github.horaciocome1.reaque.data.topics.TopicRepository
import io.github.horaciocome1.reaque.data.users.UserRepository
import io.github.horaciocome1.reaque.ui.comments.CommentsViewModelFactory
import io.github.horaciocome1.reaque.ui.favorites.FavoritesViewModelFactory
import io.github.horaciocome1.reaque.ui.posts.PostsViewModelFactory
import io.github.horaciocome1.reaque.ui.topics.TopicsViewModelFactory
import io.github.horaciocome1.reaque.ui.users.UsersVewModelFactory

object InjectorUtils {

    fun provideTopicsViewModelFactory(): TopicsViewModelFactory {
        val repository = TopicRepository.getInstance(Database.getInstance().topicWebService)
        return TopicsViewModelFactory(repository)
    }

    fun provideUsersViewModelFactory(): UsersVewModelFactory {
        val repository = UserRepository.getInstance(Database.getInstance().userWebService)
        return UsersVewModelFactory(repository)
    }

    fun providePostsViewModelFactory(): PostsViewModelFactory {
        val repository = PostRepository.getInstance(Database.getInstance().postWebService)
        return PostsViewModelFactory(repository)
    }

    fun provideFavoritesViewModelFactory(): FavoritesViewModelFactory {
        val repository = FavoriteRepository.getInstance(Database.getInstance().favoriteWebService)
        return FavoritesViewModelFactory(repository)
    }

    fun provideCommentsViewModelFactory(): CommentsViewModelFactory {
        val repository = CommentsRepository.getInstance(Database.getInstance().commentsWebService)
        return CommentsViewModelFactory(repository)
    }

}