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

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>().apply { value = User("") }
    }

    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private var topicId = ""

    private var userId = ""

    override fun update(user: User, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (user.id.isNotBlank())
            auth.addSimpleAuthStateListener {
                db.document("users/${it.uid}")
                    .set(user.mapSimple, SetOptions.merge())
                    .addOnCompleteListener(onCompleteListener)
            }
    }

    override fun get(user: User): LiveData<User> {
        if (user.id != userId && user.id.isNotBlank()) {
            this.user.value = User("")
            db.document("users/${user.id}")
                .addSafeSnapshotListener {
                    this.user.value = it.user
                }
            userId = user.id
        }
        return this.user
    }

    override fun get(topic: Topic): LiveData<List<User>> {
        if (topic.id != topicId && topic.id.isNotBlank())
            db.collection("topics/${topic.id}/users")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(100)
                .safeGet {
                    users.value = it.users
                }
        return users
    }

    fun updateRegistrationToken(token: String, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (token.isNotBlank())
            auth.addSimpleAuthStateListener {
                val data = mapOf("registrationToken" to token)
                db.document("users/${it.uid}")
                    .set(data, SetOptions.merge())
                    .addOnCompleteListener(onCompleteListener)
            }
    }

}