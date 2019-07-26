package io.github.horaciocome1.reaque.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot

fun DocumentReference.addSimpleAndSafeSnapshotListener(
    auth: FirebaseAuth,
    listener: (DocumentSnapshot, FirebaseUser) -> Unit
) {
    auth.addSimpleAuthStateListener { user -> addSimpleSnapshotListener { listener(it, user) } }
}

fun DocumentReference.addSimpleSnapshotListener(listener: (DocumentSnapshot) -> Unit) {
    addSnapshotListener { snapshot, exception ->
        if (exception == null && snapshot != null)
            listener(snapshot)
    }
}