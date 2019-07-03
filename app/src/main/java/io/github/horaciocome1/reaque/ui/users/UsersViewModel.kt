package io.github.horaciocome1.reaque.ui.users

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.util.Constants

class UsersViewModel(
    private val usersRepository: UsersRepository,
    private val subscriptionsRepository: SubscriptionsRepository
) : ViewModel() {

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
        subscriptionsRepository.subscribe(user) { view.visibility = View.GONE }
    }

    fun unSubscribe(view: View, user: User) {
        view.isEnabled = false
        subscriptionsRepository.unSubscribe(user) { view.visibility = View.GONE }
    }

    fun amSubscribedTo(user: User) = subscriptionsRepository.amSubscribedTo(user)

    fun openSubscribers(view: View, user: User) {}

    fun openSubscriptions(view: View, user: User) {}

    fun openPosts(view: View, user: User) {}

}