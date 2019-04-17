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

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.util.InjectorUtils

val MoreFragment.viewModel: MoreViewModel
    get() {
        val factory = InjectorUtils.moreViewModelFactory
        return ViewModelProviders.of(this, factory).get(MoreViewModel::class.java)
    }

class MoreViewModel(repository: UsersRepository) : ViewModel() {

    val me = repository.me

    fun openEditProfile(view: View, user: User) {
        val directions = MoreFragmentDirections.actionOpenEditProfile(user.id)
        view.findNavController().navigate(directions)
    }

    fun openPosting(view: View) {
        val directions = MoreFragmentDirections.actionOpenPosting()
        view.findNavController().navigate(directions)
    }

    fun openSettings(view: View) {
        val directions = MoreFragmentDirections.actionOpenSettings()
        view.findNavController().navigate(directions)
    }

    fun openProfile(view: View, user: User) {
        val directions = MoreFragmentDirections.actionOpenProfileFromMore(user.id)
        view.findNavController().navigate(directions)
    }

}