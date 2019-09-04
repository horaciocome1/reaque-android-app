package io.github.horaciocome1.reaque.ui.users.update

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

    val isUserReady: Boolean
        get() {
            user.run {
                return bio.isNotBlank()
                        && address.isNotBlank()
                        && bio != "null"
                        && address != "null"
            }
        }

    val navigateUp: (View) -> Unit = {
        it.findNavController()
            .navigateUp()
    }

    fun get(user: User) = repository.get(user)

    fun update(view: View): UpdateUserViewModel {
        isUpdatingUser = true
        repository.update(user) {
            navigateUp(view)
        }
        return this
    }

}