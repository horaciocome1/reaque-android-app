package io.github.horaciocome1.reaque.ui.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.Constants
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private val tag1 = "SignInActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        Glide.with(this)
            .load(getString(R.string.url_background_sign_in))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageview)
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
                    firebaseAuthWithGoogle(account!!)
                } catch (e: ApiException) {
                    Log.w(tag1, "Google sign in failed", e)
                    retry { signInWithGoogle() }
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        try {
            val googleSignInClient = GoogleSignIn.getClient(activity as MainActivity, gso)
            startActivityForResult(googleSignInClient?.signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
        } catch (ex: Exception) {
            Log.e(tag1, "This fragment is not hosted in main activity", ex)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(tag, "firebaseAuthWithGoogle ${account.id!!}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                Log.d(tag, "firebaseAuthWithGoogle:success")
                root_view.findNavController().navigateUp()
            }
            .addOnFailureListener {
                Log.w(tag, "firebaseAuthWithGoogle:failure", it)
                retry { firebaseAuthWithGoogle(account) }
            }
    }

    private fun retry(function: () -> Unit) {
        progressbar?.visibility = View.GONE
        val snackBar =
            Snackbar.make(root_view, "Ocorreu um erro inexperado! Voltar a tentar?", Snackbar.LENGTH_INDEFINITE)
        snackBar.setAction("Tentar") {
            snackBar.dismiss()
            function()
            progressbar?.visibility = View.VISIBLE
        }.show()
    }

}