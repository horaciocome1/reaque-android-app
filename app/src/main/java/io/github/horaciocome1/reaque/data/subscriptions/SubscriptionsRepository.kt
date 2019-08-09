package io.github.horaciocome1.reaque.data.subscriptions

import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.users.User

class SubscriptionsRepository private constructor(
    private val service: SubscriptionsService
) : SubscriptionsInterface {

    override fun subscribe(user: User, onCompleteListener: (Task<Void?>?) -> Unit) =
        service.subscribe(user, onCompleteListener)

    override fun unSubscribe(user: User, onCompleteListener: (Task<Void?>?) -> Unit) =
        service.unSubscribe(user, onCompleteListener)

    override fun getSubscriptions(user: User) = service.getSubscriptions(user)

    override fun getSubscribers(user: User) = service.getSubscribers(user)

    override fun amSubscribedTo(user: User) = service.amSubscribedTo(user)

    companion object {

        private var instance: SubscriptionsRepository? = null

        fun getInstance(service: SubscriptionsService) = instance ?: synchronized(this) {
            instance ?: SubscriptionsRepository(service)
                .also {
                    instance = it
                }
        }

    }

}