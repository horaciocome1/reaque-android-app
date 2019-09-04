package io.github.horaciocome1.reaque.ui.users.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsRepository
import io.github.horaciocome1.reaque.data.users.UsersRepository

class UserProfileViewModelFactory(
    private val usersRepository: UsersRepository,
    private val subscriptionsRepository: SubscriptionsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        UserProfileViewModel(usersRepository, subscriptionsRepository) as T
}