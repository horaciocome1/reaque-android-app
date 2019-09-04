package io.github.horaciocome1.reaque.ui.sign_in

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import io.github.horaciocome1.reaque.databinding.FragmentSignInBinding
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private val viewModel: SignInViewModel by lazy {
        val factory = InjectorUtils.signInViewModelFactory
        ViewModelProviders.of(this, factory)[SignInViewModel::class.java]
    }

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sign_in_button?.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            viewModel.signInWithGoogle(this)
            it.visibility = View.GONE
        }
        progressBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constants.GOOGLE_SIGN_IN_REQUEST_CODE -> {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null)
                        viewModel.firebaseAuthWithGoogle(account) {
                            if (it is Exception)
                                reset()
                            else
                                sign_in_button?.findNavController()
                                    ?.navigateUp()
                        }
                } catch (exception: ApiException) {
                    reset()
                }
            }
        }
    }

    private fun reset() {
        sign_in_button?.visibility = View.VISIBLE
        error_textview?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

}