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

package io.github.horaciocome1.reaque.ui.posts

import android.net.Uri
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.utilities.InjectorUtils

val PostsFragment.viewModel: PostsViewModel
    get() {
        val factory = InjectorUtils.postsViewModelFactory
        return ViewModelProviders.of(this, factory)[PostsViewModel::class.java]
    }

val ReadFragment.viewModel: PostsViewModel
    get() {
        val factory = InjectorUtils.postsViewModelFactory
        return ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)
    }

val PostingFragment.viewModel: PostsViewModel
    get() {
        val factory = InjectorUtils.postsViewModelFactory
        return ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)
    }

class PostsViewModel(
    private val postsRepository: PostsRepository,
    topicsRepository: TopicsRepository
) : ViewModel() {

    val post = Post("").apply {
        topic.title = "Nenhum tópico selecionado!"
    }

    var imageUri: Uri = Uri.EMPTY

    @Bindable
    val postTitle = MutableLiveData<String>()

    @Bindable
    val postMessage = MutableLiveData<String>()

    val topics = topicsRepository.topics

    fun addPost(post: Post) = postsRepository.addPost(post)

    fun getPosts(topic: Topic) = postsRepository.getPosts(topic)

    fun getPosts(user: User) = postsRepository.getPosts(user)

    fun getPosts(post: Post) = postsRepository.getPosts(post)


}