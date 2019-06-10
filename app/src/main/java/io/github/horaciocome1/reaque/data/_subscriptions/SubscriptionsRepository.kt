package io.github.horaciocome1.reaque.data._subscriptions

import io.github.horaciocome1.reaque.data._users.User

class SubscriptionsRepository private constructor(private val service: SubscriptionsService) :
    SubscriptionsServiceInterface {

    override fun subscribe(user: User, onSuccessListener: (Void) -> Unit) = service.subscribe(user, onSuccessListener)

    override fun unSubscribe(user: User, onSuccessListener: (Void) -> Unit) =
        service.unSubscribe(user, onSuccessListener)

    override fun getSubscriptions() = service.getSubscriptions()

    override fun getSubscribers() = service.getSubscribers()

    override fun amSubscribedTo(user: User) = service.amSubscribedTo(user)

    companion object {

        private var instance: SubscriptionsRepository? = null

        fun getInstance(service: SubscriptionsService) = instance
            ?: synchronized(this) {
                instance ?: SubscriptionsRepository(service).also { instance }
            }

    }

}