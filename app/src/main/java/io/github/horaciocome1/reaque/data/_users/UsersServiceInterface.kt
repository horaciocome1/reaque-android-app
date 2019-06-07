package io.github.horaciocome1.reaque.data._users

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.topics.Topic

interface UsersServiceInterface {

    fun update(user: User, onSuccessListener: (Void) -> Unit)

    fun get(user: User): LiveData<User>

    fun get(topic: Topic): LiveData<List<User>>

}