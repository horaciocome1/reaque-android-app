package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.*

class UsersService : UsersInterface {

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var _user = User("")

    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().apply { value = _user }
    }

    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private var topicId = ""

    override fun update(user: User, onCompleteListener: (Task<Void?>?) -> Unit) {
        auth.addSimpleAuthStateListener {
            val ref = db.document("users/${it.uid}")
            ref.set(user.mapSimple, SetOptions.merge()).addOnCompleteListener(onCompleteListener)
        }
    }

    override fun get(user: User): LiveData<User> {
        if (user.id != _user.id) {
            this.user.value = User("")
            val ref = db.document("users/${user.id}")
            ref.addSafeSnapshotListener {
                _user = it.user
                this.user.value = _user
            }
        }
        return this.user
    }

    override fun get(topic: Topic): LiveData<List<User>> {
        if (topic.id != topicId) {
            val ref = db.collection("topics/${topic.id}/users")
            ref.orderBy("score", Query.Direction.DESCENDING).safeGet {
                users.value = it.users
            }
        }
        return users
    }

}