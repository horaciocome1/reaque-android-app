package io.github.horaciocome1.reaque.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

fun Query.safeGet(listener: (QuerySnapshot) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.addSimpleAuthStateListener {
        get().addOnSuccessListener { it?.let { listener(it) } }
    }
}

fun Query.addSimpleSnapshotListener(listener: (QuerySnapshot) -> Unit) {
    addSnapshotListener { snapshot, exception ->
        if (exception == null && snapshot != null)
            listener(snapshot)
    }
}