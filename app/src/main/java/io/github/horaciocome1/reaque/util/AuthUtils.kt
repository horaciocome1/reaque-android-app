package io.github.horaciocome1.reaque.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

fun FirebaseAuth.addSimpleAuthStateListener(l: (FirebaseUser) -> Unit) {
    var firstTime = true
    val listener = FirebaseAuth.AuthStateListener {
        if (currentUser != null && firstTime) {
            l(currentUser!!)
            firstTime = false
        }
    }
    removeAuthStateListener(listener)
    addAuthStateListener(listener)
}