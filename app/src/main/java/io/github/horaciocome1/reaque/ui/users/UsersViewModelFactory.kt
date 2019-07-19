package io.github.horaciocome1.reaque.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository

class UsersViewModelFactory(
    private val usersRepository: UsersRepository,
    private val subscriptionsRepository: SubscriptionsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        UsersViewModel(usersRepository, subscriptionsRepository) as T
}