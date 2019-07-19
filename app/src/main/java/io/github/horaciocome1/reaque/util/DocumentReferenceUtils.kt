package io.github.horaciocome1.reaque.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentReference.addSimpleAndSafeSnapshotListener(
    TAG: String,
    auth: FirebaseAuth,
    listener: (DocumentSnapshot, FirebaseUser) -> Unit
) {
    auth.addSimpleAuthStateListener { user ->
        addSimpleSnapshotListener(TAG) {
            listener(it, user)
        }
    }
}

fun DocumentReference.addSimpleSnapshotListener(TAG: String, listener: (DocumentSnapshot) -> Unit) {
    addSnapshotListener { snapshot, exception ->
        when {
            exception != null -> onListeningFailed(TAG, exception)
            snapshot != null -> listener(snapshot)
            else -> onSnapshotNull(TAG)
        }
    }
}