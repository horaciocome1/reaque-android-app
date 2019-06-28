package io.github.horaciocome1.reaque.ui.users

import android.view.View
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.util.ObservableViewModel

class UpdateUserViewModel(private val repository: UsersRepository) : ObservableViewModel() {

    val user = User("")

    @Bindable
    val bio = MutableLiveData<String>()

    @Bindable
    val address = MutableLiveData<String>()

    var isUpdatingUser = false

    fun update(view: View, user: User) {
        isUpdatingUser = true
        repository.update(user) { navigateUp(view) }
    }

    private fun navigateUp(view: View) = view.findNavController().navigateUp()

}