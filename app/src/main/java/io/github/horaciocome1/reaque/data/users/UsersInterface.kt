package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import io.github.horaciocome1.reaque.data.topics.Topic

interface UsersInterface {

    fun update(user: User, onSuccessListener: (Void?) -> Unit)

    fun get(user: User): LiveData<User>

    fun get(topic: Topic): LiveData<List<User>>

}