package io.github.horaciocome1.reaque.ui.users

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.subscriptions.SubscriptionsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.data.users.UsersRepository
import io.github.horaciocome1.reaque.util.Constants

class UsersViewModel(
    private val usersRepository: UsersRepository,
    private val subscriptionsRepository: SubscriptionsRepository
) : ViewModel() {

    var users: List<User> = mutableListOf()

    val onItemClickListener: (View, Int) -> Unit = { view, position ->
        if (users.isNotEmpty()) {
            val directions = UsersFragmentDirections.actionOpenUserProfileFromUsers(users[position].id)
            view.findNavController().navigate(directions)
        }
    }

    fun setUsers(users: List<User>): UsersViewModel {
        this.users = users
        return this
    }

    fun get(parentId: String, requestId: String): LiveData<List<User>> {
        return when (requestId) {
            Constants.SUBSCRIPTIONS_REQUEST -> subscriptionsRepository.getSubscriptions(User(parentId))
            Constants.SUBSCRIBERS_REQUEST -> subscriptionsRepository.getSubscribers(User(parentId))
            else -> usersRepository.get(Topic(parentId))
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

    fun openSubscribers(view: View, user: User) {
        if (user.subscribers.isNotBlank() && user.subscribers != "0") {
            val directions = UserProfileFragmentDirections.actionOpenUsersFromUserProfile(
                user.id, Constants.SUBSCRIBERS_REQUEST
            )
            view.findNavController().navigate(directions)
        }
    }

    fun openSubscriptions(view: View, user: User) {
        if (user.subscriptions.isNotBlank() && user.subscriptions != "0") {
            val directions = UserProfileFragmentDirections.actionOpenUsersFromUserProfile(
                user.id, Constants.SUBSCRIPTIONS_REQUEST
            )
            view.findNavController().navigate(directions)
        }
    }

    fun openPosts(view: View, user: User) {
        if (user.posts.isNotBlank() && user.posts != "0") {
            val directions = UserProfileFragmentDirections.actionOpenPostsFromUserProfile(
                user.id, Constants.USER_POSTS_REQUEST
            )
            view.findNavController().navigate(directions)
        }
    }

}