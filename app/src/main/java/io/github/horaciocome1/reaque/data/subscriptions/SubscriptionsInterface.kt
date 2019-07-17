package io.github.horaciocome1.reaque.data.subscriptions

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.Transaction
import io.github.horaciocome1.reaque.data.users.User

interface SubscriptionsInterface {

    fun subscribe(user: User, onSuccessListener: (Transaction?) -> Unit)

    fun unSubscribe(user: User, onSuccessListener: (Void?) -> Unit)

    fun getSubscriptions(user: User): LiveData<List<User>>

    fun getSubscribers(user: User): LiveData<List<User>>

    fun amSubscribedTo(user: User): LiveData<Boolean>

}