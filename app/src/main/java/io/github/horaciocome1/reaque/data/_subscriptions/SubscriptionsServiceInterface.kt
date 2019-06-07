package io.github.horaciocome1.reaque.data._subscriptions

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data._users.User

interface SubscriptionsServiceInterface {

    fun subscribe(user: User, onSuccessListener: (Void) -> Unit)

    fun unSubscribe(user: User, onSuccessListener: (Void) -> Unit)

    fun getSubscriptions(): LiveData<List<User>>

    fun getSubscribers(): LiveData<List<User>>

    fun amSubscribedTo(user: User): LiveData<Boolean>

}