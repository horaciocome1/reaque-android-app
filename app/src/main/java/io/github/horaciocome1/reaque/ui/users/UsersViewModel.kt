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

package io.github.horaciocome1.reaque.ui.users

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.ui.signin.SignInActivity
import io.github.horaciocome1.reaque.utilities.Constants
import io.github.horaciocome1.reaque.utilities.InjectorUtils
import io.github.horaciocome1.reaque.utilities.ObservableViewModel
import jp.wasabeef.glide.transformations.BlurTransformation

val UsersFragment.viewModel: UsersViewModel
    get() {
        val factory = InjectorUtils.usersViewModelFactory
        return ViewModelProviders.of(this, factory).get(UsersViewModel::class.java)
    }

val ProfileFragment.viewModel: UsersViewModel
    get() {
        val factory = InjectorUtils.usersViewModelFactory
        return ViewModelProviders.of(this, factory).get(UsersViewModel::class.java)
    }

val SignInActivity.viewModel: UsersViewModel
    get() {
        val factory = InjectorUtils.usersViewModelFactory
        return ViewModelProviders.of(this, factory).get(UsersViewModel::class.java)
    }

class UsersViewModel(
    private val usersRepository: UsersRepository,
    topicsRepository: TopicsRepository,
    private val postsRepository: PostsRepository
) : ObservableViewModel() {

    val topics = topicsRepository.topics

    val favorites = usersRepository.favorites

    fun addUser(onSuccessful: () -> Unit) = usersRepository.addUser(onSuccessful)

    fun getPosts(user: User) = postsRepository.getPosts(user)

    fun getUsers(topic: Topic) = usersRepository.getUsers(topic)

    fun getUsers(user: User) = usersRepository.getUsers(user)

    fun addToFavorites(view: View, user: User) {
        if (user.id.isNotBlank()) {
            view.isEnabled = false
            usersRepository.addToFavorites(user) {
                view.isEnabled = true
            }
        }
    }

    fun openViewer(view: View, user: User) {
        if (user.pic.isNotBlank()) {
            val directions = ProfileFragmentDirections.actionOpenViewerFromProfile(user.pic)
            view.findNavController().navigate(directions)
        }
    }

    companion object {
        @BindingAdapter("url", "type")
        @JvmStatic
        fun ImageView.loadImage(url: String?, type: Int?) {
            if (!url.isNullOrBlank())
                Glide.with(context).load(url)
                    .apply(
                        when (type) {
                            Constants.PROFILE_BLUR -> RequestOptions.bitmapTransform(BlurTransformation(7, 14))
                            Constants.PROFILE_CIRCLE -> RequestOptions.circleCropTransform()
                            else -> RequestOptions()
                        }
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
        }
    }

}