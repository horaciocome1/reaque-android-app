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

package io.github.horaciocome1.reaque.ui._more

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.user

class MoreViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    val user = MutableLiveData<User>()
        get() {
            auth.addSimpleAuthStateListener { field.value = it.user }
            return field
        }

    fun openUpdateUser(view: View) {
        if (auth.currentUser != null) {
            val directions = MoreFragmentDirections.actionOpenEditProfile()
            view.findNavController().navigate(directions)
        }
    }

    fun openCreatePost(view: View) {
        if (auth.currentUser != null) {
            val directions = MoreFragmentDirections.actionOpenPosting()
            view.findNavController().navigate(directions)
        }
    }

    fun openUserProfile(view: View, user: User) {
        if (auth.currentUser != null) {
            val directions = MoreFragmentDirections.actionOpenProfileFromMore(user.id)
            view.findNavController().navigate(directions)
        }
    }

}