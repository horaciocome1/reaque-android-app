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
import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data._posts.Post
import io.github.horaciocome1.reaque.data.favorites.FavoritesRepository
import io.github.horaciocome1.reaque.data.media.ImageUploader
import io.github.horaciocome1.reaque.data.media.MediaRepository
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.reaque.util.ObservableViewModel

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
    private val usersRepository: UsersRepository,
    private val favoritesRepository: FavoritesRepository
) : ObservableViewModel() {

    // PostingFragment
    val post = Post("").apply {
        topic.title = "Selecione um tópico."
    }
    var imageUri: Uri = Uri.EMPTY

    @Bindable
    val title = MutableLiveData<String>()

    @Bindable
    val message = MutableLiveData<String>()

    val notEmptyTopicsForPosts = topicsRepository.notEmptyTopicsForPosts

    val topics = topicsRepository.topics

    val favorites = postsRepository.favorites

    var isSubmittingPost = false

    fun isThisFavoriteForMe(post: Post) = postsRepository.isThisFavoriteForMe(post)

    fun getPosts(topic: Topic) = postsRepository.getPosts(topic)

    fun getPosts(post: Post) = postsRepository.getPosts(post)

    fun submitPost(view: View): PostsViewModel {
        isSubmittingPost = true
        val uploader = ImageUploader()
        uploader.let {
            it.imageUri = imageUri
            it.post = post
            it.onSuccessListener = { link ->
                post.pic = link
                postsRepository.submitPost(post) {
                    navigateUp(view)
                }
            }
            it.onFailureListener = {
                // caso o upload falhe
            }
        }
        mediaRepository.uploadImage(uploader)
        return this
    }

    fun addToFavorites(view: View, post: Post) {
        view.visibility = View.GONE
        favoritesRepository.addToFavorites(post)
    }

    fun removeFromFavorites(view: View, post: Post) {
        view.visibility = View.GONE
        favoritesRepository.removeFromFavorites(post)
    }

    fun openViewer(view: View, post: Post) {
        if (post.pic.isNotBlank()) {
            val directions = ReadFragmentDirections.actionOpenViewerFromRead(post.pic)
            view.findNavController().navigate(directions)
        }
    }

    fun openProfile(view: View, post: Post) {
        val directions = ReadFragmentDirections.actionOpenProfileFromRead(post.user.id)
        view.findNavController().navigate(directions)
    }

    fun navigateUp(view: View) = view.findNavController().navigateUp()

}