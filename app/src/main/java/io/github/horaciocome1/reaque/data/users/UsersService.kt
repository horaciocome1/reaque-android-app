package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class UsersService : UsersServiceInterface {

    private val tag: String by lazy { "UsersService" }

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("users") }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var _user = User("")

    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().apply { value = _user }
    }

    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private var topicId = ""

    override fun update(user: User, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document(it.uid).set(user.map, SetOptions.merge()).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun get(user: User): LiveData<User> {
        if (user.id != _user.id)
            ref.document(user.id).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                _user = snapshot.user
                this.user.value = _user
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