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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.ui.more.MoreFragment
import io.github.horaciocome1.reaque.utilities.InjectorUtils

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

val MyProfileFragment.viewModel: UsersViewModel
    get() {
        val factory = InjectorUtils.usersViewModelFactory
        return ViewModelProviders.of(this, factory).get(UsersViewModel::class.java)
    }

val MoreFragment.viewModel: UsersViewModel
    get() {
        val factory = InjectorUtils.usersViewModelFactory
        return ViewModelProviders.of(this, factory).get(UsersViewModel::class.java)
    }

class UsersViewModel(private val usersRepository: UsersRepository) : ViewModel() {

    fun addUser(user: User) = usersRepository.addUser(user)

    fun getUsers(topic: Topic) = usersRepository.getUsers(topic)

    fun getUsers(user: User) = usersRepository.getUsers(user)

    val me = usersRepository.me

}