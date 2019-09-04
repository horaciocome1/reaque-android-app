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

package io.github.horaciocome1.reaque.ui.more

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.configurations.ConfigurationsRepository
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.user

class MoreViewModel(
    bookmarksRepository: BookmarksRepository,
    configurationsRepository: ConfigurationsRepository
) : ViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val _user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().apply {
            value = User("")
        }
    }

    val user: LiveData<User>
        get() {
            if (auth.currentUser != null)
                _user.value = auth.currentUser!!.user
            return _user
        }

    val hasBookmarks = bookmarksRepository.hasBookmarks()

    val isUpdateAvailable = configurationsRepository.isUpdateAvailable()

    val getLatestVersionName = configurationsRepository.getLatestVersionName()

    fun openUpdateUser(view: View) {
        auth.currentUser?.let {
            val directions = MoreFragmentDirections
                .actionOpenUpdateUserFromMore(it.uid)
            view.findNavController()
                .navigate(directions)
        }
    }

    fun openCreatePost(view: View) {
        if (auth.currentUser != null) {
            val directions = MoreFragmentDirections
                .actionOpenCreatePostFromMore()
            view.findNavController()
                .navigate(directions)
        }
    }

    fun openUserProfile(view: View) {
        auth.currentUser?.let {
            val directions = MoreFragmentDirections
                .actionOpenUserProfileFromMore(it.uid)
            view.findNavController()
                .navigate(directions)
        }
    }

    fun openBookmarks(view: View) {
        auth.currentUser?.let {
            val directions = MoreFragmentDirections
                .actionOpenPostsFromMore(it.uid, Constants.BOOKMARKS_REQUEST)
            view.findNavController()
                .navigate(directions)
        }
    }

    fun openUrl(fragment: MoreFragment, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        fragment.startActivity(intent)
    }

    fun sendEmail(fragment: MoreFragment, email: String, onFailureListener: (Exception) -> Unit) {
        try {
            val mailto = "mailto:$email"
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse(mailto)
            fragment.startActivity(emailIntent)
        } catch (exception: Exception) {
            onFailureListener(exception)
        }
    }

}