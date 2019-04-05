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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.media.ImageUploader
import io.github.horaciocome1.reaque.data.media.MediaRepository
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository
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
    topicsRepository: TopicsRepository,
    private val mediaRepository: MediaRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {

    // PostingFragment
    val post = Post("").apply {
        topic.title = "Selecione um tópico."
    }
    var imageUri: Uri = Uri.EMPTY
    @Bindable
    val postTitle = MutableLiveData<String>()
    @Bindable
    val postMessage = MutableLiveData<String>()
    val topics = topicsRepository.topics
    private val _isFinished = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isFinished: LiveData<Boolean>
        get() = _isFinished
    private val _isSubmitting = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isSubmitting: LiveData<Boolean>
        get() = _isSubmitting

    // ReadFragment
    private val _post = Post("")
    private val _isAddingToFavorites = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isAddingToFavorites: LiveData<Boolean>
        get() = _isAddingToFavorites


    val favorites = postsRepository.favorites

    fun getPosts(topic: Topic) = postsRepository.getPosts(topic)

    fun getPosts(post: Post): LiveData<Post> {
        _post.id = post.id
        return postsRepository.getPosts(post)
    }

    fun submitPost() {
        _isSubmitting.value = true
        val uploader = ImageUploader().apply {
            imageUri = this@PostsViewModel.imageUri
            post = this@PostsViewModel.post
            onComplete = { link: String ->
                this@PostsViewModel.post.run {
                    pic = link
                    postsRepository.submitPost(this) {
                        usersRepository.addTopicToUser(topic) {
                            _isFinished.value = true
                        }
                    }
                }
            }
            onFailure = {
                // caso o upload falhe
            }
        }
        mediaRepository.uploadImage(uploader)
    }

    fun addToFavorites() {
        _isAddingToFavorites.value = true
        postsRepository.addToFavorites(_post) {
            usersRepository.addToFavorites(_post) {
                _isAddingToFavorites.value = false
            }
        }
    }

}