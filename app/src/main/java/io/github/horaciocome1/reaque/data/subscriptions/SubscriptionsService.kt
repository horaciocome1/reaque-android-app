package io.github.horaciocome1.reaque.data.subscriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.*

class SubscriptionsService : SubscriptionsServiceInterface {

    private val tag: String by lazy { "SubscriptionsService" }

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("subscriptions") }

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

    override fun subscribe(user: User, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            val subscription = Subscription("").apply {
                this.user = user
                subscriber = it.user
            }
            ref.document("${it.uid}_${user.id}").set(subscription.map).addOnSuccessListener(onSuccessListener)
        }
    }

    override fun unSubscribe(user: User, onSuccessListener: (Void?) -> Unit) {
        auth.addSimpleAuthStateListener {
            ref.document("${it.uid}_${user.id}").delete().addOnSuccessListener(onSuccessListener)
        }
    }

    override fun getSubscriptions(): LiveData<List<User>> {
        auth.addSimpleAuthStateListener { user ->
            ref.whereEqualTo("subscriber.id", user.uid).addSimpleSnapshotListener(tag) {
                subscriptions.value = it.subscriptions
            }
        }
        return subscriptions
    }

    override fun getSubscribers(): LiveData<List<User>> {
        auth.addSimpleAuthStateListener { user ->
            ref.whereEqualTo("user.id", user.uid).addSimpleSnapshotListener(tag) {
                subscribers.value = it.subscribers
            }
        }
        return subscribers
    }

    override fun amSubscribedTo(user: User): LiveData<Boolean> {
        amSubscribedTo.value = false
        auth.addSimpleAuthStateListener { firebaseUser ->
            ref.document("${firebaseUser.uid}_${user.id}").addSimpleSnapshotListener(tag) {
                val userId = it["user.id"]
                amSubscribedTo.value = userId != null
            }
        }
        return amSubscribedTo
    }

}