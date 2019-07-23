package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import com.google.android.gms.tasks.Task
import io.github.horaciocome1.reaque.data.topics.Topic

interface UsersInterface {

    fun update(user: User, onCompleteListener: (Task<Void?>?) -> Unit)

    fun get(user: User): LiveData<User>

    fun get(topic: Topic): LiveData<List<User>>

}