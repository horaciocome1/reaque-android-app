package io.github.horaciocome1.reaque.data.subscriptions

import com.google.firebase.firestore.Transaction
import io.github.horaciocome1.reaque.data.users.User

class SubscriptionsRepository private constructor(private val service: SubscriptionsService) :
    SubscriptionsInterface {

    override fun subscribe(user: User, onSuccessListener: (Transaction?) -> Unit) =
        service.subscribe(user, onSuccessListener)

    override fun unSubscribe(user: User, onSuccessListener: (Void?) -> Unit) =
        service.unSubscribe(user, onSuccessListener)

    override fun getSubscriptions(user: User) = service.getSubscriptions(user)

    override fun getSubscribers(user: User) = service.getSubscribers(user)

    override fun amSubscribedTo(user: User) = service.amSubscribedTo(user)

    companion object {

        private var instance: SubscriptionsRepository? = null

        fun getInstance(service: SubscriptionsService) = instance
            ?: synchronized(this) {
                instance ?: SubscriptionsRepository(service).also { instance }
            }

    }

}