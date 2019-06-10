package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class UsersService : UsersServiceInterface {

    private val tag = "UsersService"

    private val ref = FirebaseFirestore.getInstance().collection("users")

    private val auth = FirebaseAuth.getInstance()

    private val user = MutableLiveData<User>().apply { value = User("") }

    private val users = MutableLiveData<List<User>>().apply { value = mutableListOf() }

    private var topicId = ""

    override fun update(user: User, onSuccessListener: (Void) -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document(it.uid).set(user.map, SetOptions.merge()).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun get(user: User): LiveData<User> {
        this.user.value?.let {
            if (user.id != it.id)
                ref.document(user.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                    this.user.value = snapshot.user
                }
        }
        return this.user
    }

    override fun get(topic: Topic): LiveData<List<User>> {
        if (topic.id != topicId)
            ref.whereEqualTo("topic.id", topic.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                users.value = snapshot.users
            }
        return users
    }

}