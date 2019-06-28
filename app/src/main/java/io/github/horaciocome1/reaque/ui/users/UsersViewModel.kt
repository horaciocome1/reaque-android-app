package io.github.horaciocome1.reaque.ui.users

import android.view.View
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.ObservableViewModel

class UsersViewModel(
    private val usersRepository: UsersRepository,
    private val subscriptionsRepository: SubscriptionsRepository
) : ObservableViewModel() {

    fun get(id: String): LiveData<List<User>> {
        return when (id) {
            Constants.SUBSCRIPTIONS_REQUEST -> subscriptionsRepository.getSubscriptions()
            Constants.SUBSCRIBERS_REQUEST -> subscriptionsRepository.getSubscribers()
            else -> usersRepository.get(Topic(id))
        }
    }

    fun get(user: User) = usersRepository.get(user)

    fun subscribe(view: View, user: User) {
        view.isEnabled = false
        subscriptionsRepository.subscribe(user) { view.isEnabled = true }
    }

    fun unSubscribe(view: View, user: User) {
        view.isEnabled = false
        subscriptionsRepository.unSubscribe(user) { view.isEnabled = true }
    }

    fun amSubscribedTo(user: User) = subscriptionsRepository.amSubscribedTo(user)

    fun navigateUp(view: View) = view.findNavController().navigateUp()

}