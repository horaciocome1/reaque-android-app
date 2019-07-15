package io.github.horaciocome1.reaque.ui.users.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.users.UsersRepository

class UpdateUserViewModelFactory(private val repository: UsersRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = UpdateUserViewModel(
        repository
    ) as T

}