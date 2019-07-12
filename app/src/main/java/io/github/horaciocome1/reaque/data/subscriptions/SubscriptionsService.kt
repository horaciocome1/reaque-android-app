package io.github.horaciocome1.reaque.data.subscriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.addSimpleAuthStateListener
import io.github.horaciocome1.reaque.util.addSimpleSnapshotListener
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.users

class SubscriptionsService : SubscriptionsInterface {

    private val tag: String by lazy { "SubscriptionsService" }

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val subscriptions: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private val subscribers: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private val amSubscribedTo: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }

    private val subscriptionsOf = ""

    private val subscribersOf = ""

    override fun subscribe(user: User, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            val ref = db.document("users/${it.uid}/subscriptions/${user.id}")
            ref.set(user.map).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun unSubscribe(user: User, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            val ref = db.document("users/${it.uid}/subscriptions/${user.id}")
            ref.delete().addOnSuccessListener(onSuccessListener)
        }
    }

    override fun getSubscriptions(user: User): LiveData<List<User>> {
        if (user.id != subscriptionsOf) {
            val ref = db.collection("users/${user.id}/subscriptions")
            ref.orderBy("timestamp", Query.Direction.DESCENDING).addSimpleSnapshotListener(tag) {
                subscriptions.value = it.users
            }
        }
        return subscriptions
    }

    override fun getSubscribers(user: User): LiveData<List<User>> {
        if (user.id != subscribersOf) {
            val ref = db.collection("users/${user.id}/subscribers")
            ref.orderBy("timestamp", Query.Direction.DESCENDING).addSimpleSnapshotListener(tag) {
                subscribers.value = it.users
            }
        }
        return subscribers
    }

    override fun amSubscribedTo(user: User): LiveData<Boolean> {
        amSubscribedTo.value = false
        auth.addSimpleAuthStateListener { firebaseUser ->
            val ref = db.document("users/${firebaseUser.uid}/subscriptions/${user.id}")
            ref.addSimpleSnapshotListener(tag) {
                val timestamp = it["timestamp"]
                amSubscribedTo.value = timestamp != null
            }
        }
        return amSubscribedTo
    }

}