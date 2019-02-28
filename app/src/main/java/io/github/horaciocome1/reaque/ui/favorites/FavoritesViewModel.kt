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

package io.github.horaciocome1.reaque.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.utilities.InjectorUtils

val FavoritesFragment.viewModel: FavoritesViewModel
    get() {
        val factory = InjectorUtils.favoritesViewModelFactory
        return ViewModelProviders.of(this, factory)[FavoritesViewModel::class.java]
    }

class FavoritesViewModel(
    private val topicsRepository: TopicsRepository,
    postsRepository: PostsRepository,
    usersRepository: UsersRepository
) : ViewModel() {

    val topics = topicsRepository.favorites

    fun getTopics(topic: Topic) = topicsRepository.getTopics(topic)

    val posts = postsRepository.favorites

    val users = usersRepository.favorites

}