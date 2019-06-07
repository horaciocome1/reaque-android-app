package io.github.horaciocome1.reaque.data._users

import io.github.horaciocome1.reaque.data.topics.Topic

class UsersRepository private constructor(private val service: UsersService) : UsersServiceInterface {

    override fun update(user: User, onSuccessListener: (Void) -> Unit) = service.update(user, onSuccessListener)

    override fun get(user: User) = service.get(user)

    override fun get(topic: Topic) = service.get(topic)

    companion object {

        @Volatile
        private var instance: UsersRepository? = null

        fun getInstance(service: UsersService) = instance
            ?: synchronized(this) {
                instance ?: UsersRepository(service).also { instance = it }
            }

    }

}