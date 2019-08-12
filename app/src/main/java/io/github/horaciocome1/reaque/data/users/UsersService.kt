package io.github.horaciocome1.reaque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.util.mapSimple
import io.github.horaciocome1.reaque.util.user
import io.github.horaciocome1.reaque.util.users

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
        if (auth.currentUser != null)
            db.document("users/${auth.currentUser!!.uid}")
                .set(user.mapSimple, SetOptions.merge())
                .addOnCompleteListener(onCompleteListener)
    }

    override fun get(user: User): LiveData<User> {
        if (user.id != userId && user.id.isNotBlank() && auth.currentUser != null) {
            this.user.value = User("")
            db.document("users/${user.id}")
                .addSnapshotListener { snapshot, exception ->
                    if (exception == null && snapshot != null)
                        this.user.value = snapshot.user
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
                .get()
                .addOnSuccessListener {
                    users.value = it.users
                }
        return users
    }

    fun updateRegistrationToken(token: String, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (token.isNotBlank() && auth.currentUser != null) {
            val data = mapOf("registrationToken" to token)
            db.document("users/${auth.currentUser!!.uid}")
                .set(data, SetOptions.merge())
                .addOnCompleteListener(onCompleteListener)
        }
    }

}