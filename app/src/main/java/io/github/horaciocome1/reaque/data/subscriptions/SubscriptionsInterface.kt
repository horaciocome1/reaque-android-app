package io.github.horaciocome1.reaque.data.subscriptions

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.users.User

interface SubscriptionsInterface {

    fun subscribe(user: User, onSuccessListener: (Void?) -> Unit)

    fun unSubscribe(user: User, onSuccessListener: (Void?) -> Unit)

    fun getSubscriptions(): LiveData<List<User>>

    fun getSubscribers(): LiveData<List<User>>

    fun amSubscribedTo(user: User): LiveData<Boolean>

}