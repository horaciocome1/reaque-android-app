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

package io.github.horaciocome1.reeque.utilities

import io.github.horaciocome1.reeque.data.Database
import io.github.horaciocome1.reeque.data.post.PostRepository
import io.github.horaciocome1.reeque.data.topic.TopicRepository
import io.github.horaciocome1.reeque.data.user.UserRepository
import io.github.horaciocome1.reeque.ui.posts.PostsViewModelFactory
import io.github.horaciocome1.reeque.ui.topics.TopicsViewModelFactory
import io.github.horaciocome1.reeque.ui.users.UsersVewModelFactory

object InjectorUtils {

    fun provideTopicsViewModelFactory(): TopicsViewModelFactory {
        val repository = TopicRepository.getInstance(Database.getInstance().topicDAO)
        return TopicsViewModelFactory(repository)
    }

    fun provideUsersViewModelFactory(): UsersVewModelFactory {
        val repository = UserRepository.getInstance(Database.getInstance().userDAO)
        return UsersVewModelFactory(repository)
    }

    fun providePostsViewModelFactory(): PostsViewModelFactory {
        val repository = PostRepository.getInstance(Database.getInstance().postDAO)
        return PostsViewModelFactory(repository)
    }

}