package io.github.horaciocome1.reaque.ui.sign_in

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.databinding.FragmentSignInBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.Constants
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private val tag1 = "SignInActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding.url = getString(R.string.url_background_sign_in)
    }

    override fun onStart() {
        super.onStart()
        signInWithGoogle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.GOOGLE_SIGN_IN_REQUEST_CODE -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.let { firebaseAuthWithGoogle(account) }
                } catch (exception: ApiException) {
                    Log.w(tag1, "Google sign in failed", exception)
                    retry()
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        if (activity is MainActivity) {
            val googleSignInClient = GoogleSignIn.getClient(activity as MainActivity, gso)
            startActivityForResult(googleSignInClient?.signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        account.id?.let { Log.d(tag, "firebaseAuthWithGoogle ${account.id}") }
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Log.d(tag, "firebaseAuthWithGoogle:success")
                root_view?.findNavController()?.navigateUp()
            }
            .addOnFailureListener {
                Log.w(tag, "firebaseAuthWithGoogle:failure", it)
                retry()
            }
    }

    private fun retry() {
        progressbar?.visibility = View.GONE
        root_view?.let {
            val snackBar = Snackbar
                .make(it, "Ocorreu um erro inexperado! Voltar a tentar?", Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("Tentar") {
                snackBar.dismiss()
                signInWithGoogle()
                progressbar?.visibility = View.VISIBLE
            }.show()
        }
    }

}