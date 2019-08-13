package io.github.horaciocome1.reaque.data.subscriptions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.map
import io.github.horaciocome1.reaque.util.user
import io.github.horaciocome1.reaque.util.users

class SubscriptionsService : SubscriptionsInterface {

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val incrementSubscriptions: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("subscriptions" to increment)
    }

    private val incrementSubscribers: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(1)
        mapOf("subscribers" to increment)
    }

    private val decrementSubscriptions: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(-1)
        mapOf("subscriptions" to increment)
    }

    private val decrementSubscribers: Map<String, FieldValue> by lazy {
        val increment = FieldValue.increment(-1)
        mapOf("subscribers" to increment)
    }

    private val subscriptions: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private val subscribers: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().apply { value = mutableListOf() }
    }

    private val amSubscribedTo: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply { value = false }
    }

    private var subscriptionsOf = ""

    private var subscribersOf = ""

    override fun subscribe(user: User, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (user.id.isNotBlank() && auth.currentUser != null) {
            val subscriptionRef = db.document("users/${auth.currentUser!!.uid}/subscriptions/${user.id}")
            val subscriberRef = db.document("users/${user.id}/subscribers/${auth.currentUser!!.uid}")
            val myRef = db.document("users/${auth.currentUser!!.uid}")
            val hisRef = db.document("users/${user.id}")
            db.runBatch {
                it.set(subscriptionRef, user.map)
                it.set(subscriberRef, auth.currentUser!!.user.map)
                it.set(myRef, incrementSubscriptions, SetOptions.merge())
                it.set(hisRef, incrementSubscribers, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun unSubscribe(user: User, onCompleteListener: (Task<Void?>?) -> Unit) {
        if (user.id.isNotBlank() && auth.currentUser != null) {
            val subscriptionRef = db.document("users/${auth.currentUser!!.uid}/subscriptions/${user.id}")
            val subscriberRef = db.document("users/${user.id}/subscribers/${auth.currentUser!!.uid}")
            val myRef = db.document("users/${auth.currentUser!!.uid}")
            val hisRef = db.document("users/${user.id}")
            db.runBatch {
                it.delete(subscriptionRef)
                it.delete(subscriberRef)
                it.set(myRef, decrementSubscriptions, SetOptions.merge())
                it.set(hisRef, decrementSubscribers, SetOptions.merge())
            }.addOnCompleteListener(onCompleteListener)
        }
    }

    override fun getSubscriptions(user: User): LiveData<List<User>> {
        if (user.id != subscriptionsOf && user.id.isNotBlank()) {
            db.collection("users/${user.id}/subscriptions")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnSuccessListener {
                    if (it != null)
                        subscriptions.value = it.users
                }
            subscriptionsOf = user.id
        }
        return subscriptions
    }

    override fun getSubscribers(user: User): LiveData<List<User>> {
        if (user.id != subscribersOf && user.id.isNotBlank()) {
            db.collection("users/${user.id}/subscribers")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnSuccessListener {
                    if (it != null)
                        subscribers.value = it.users
                }
            subscribersOf = user.id
        }
        return subscribers
    }

    override fun amSubscribedTo(user: User): LiveData<Boolean> {
        amSubscribedTo.value = false
        if (user.id.isNotBlank() && auth.currentUser != null)
            db.document("users/${auth.currentUser!!.uid}/subscriptions/${user.id}")
                .addSnapshotListener { snapshot, exception ->
                    if (exception == null && snapshot != null)
                        amSubscribedTo.value = snapshot.exists()
                }
        return amSubscribedTo
    }

}