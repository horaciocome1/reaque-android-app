/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.ui.signin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.users.viewModel
import io.github.horaciocome1.reaque.utilities.Constants

fun Context.getSignInActivityIntent() = Intent(this, SignInActivity::class.java)

class SignInActivity : AppCompatActivity() {

    private val tag = "SignInActivity"
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestId()
            .requestEmail()
            .requestProfile()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val sinInItent = googleSignInClient.signInIntent
        startActivityForResult(sinInItent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.GOOGLE_SIGN_IN_REQUEST_CODE -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    account?.let {
                        firebaseAuthWithGoogle(it)
                    }
                } catch (e: ApiException) {
                    Log.w(tag, "Google sign in failed", e)
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(tag, "firebaseAuthWithGoogle ${account.id}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth = FirebaseAuth.getInstance()
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        Log.d(tag, "firebaseAuthWithGoogle:success")
                        viewModel.addUser {
                            setResult(Activity.RESULT_OK)
                            finish()
                        }
                    }
                    else -> {
                        Log.w(tag, "firebaseAuthWithGoogle:failure", it.exception)
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }
            }
    }

}