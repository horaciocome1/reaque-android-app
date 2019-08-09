package io.github.horaciocome1.reaque.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentReference.addSafeSnapshotListener(listener: (DocumentSnapshot) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.addSimpleAuthStateListener {
        addSimpleSnapshotListener { listener(it) }
    }
}

fun DocumentReference.safeGet(listener: (DocumentSnapshot) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.addSimpleAuthStateListener {
        get().addOnSuccessListener {
            listener(it)
        }
    }
}

fun DocumentReference.addSimpleSnapshotListener(listener: (DocumentSnapshot) -> Unit) {
    addSnapshotListener { snapshot, exception ->
        if (exception == null && snapshot != null)
            listener(snapshot)
    }
}
