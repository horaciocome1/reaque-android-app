package io.github.horaciocome1.reaque.data.subscriptions

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.users.User

interface SubscriptionsInterface {

    fun subscribe(user: User, onCompleteListener: (Task<Void?>?) -> Unit)

    fun unSubscribe(user: User, onCompleteListener: (Task<Void?>?) -> Unit)

    fun getSubscriptions(user: User): LiveData<List<User>>

    fun getSubscribers(user: User): LiveData<List<User>>

    fun amSubscribedTo(user: User): LiveData<Int>

}