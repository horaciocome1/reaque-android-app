package io.github.horaciocome1.reaque.ui.sign_in

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.Constants

class SignInViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    fun signInWithGoogle(fragment: SignInFragment) {
        val activity = fragment.activity
        if (activity is MainActivity) {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            val googleSignInClient = GoogleSignIn.getClient(activity, gso)
            fragment.startActivityForResult(
                googleSignInClient?.signInIntent,
                Constants.GOOGLE_SIGN_IN_REQUEST_CODE
            )
        }
    }

    fun firebaseAuthWithGoogle(
        account: GoogleSignInAccount,
        listener: (Any) -> Unit
    ) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener(listener)
            .addOnFailureListener(listener)
    }


}