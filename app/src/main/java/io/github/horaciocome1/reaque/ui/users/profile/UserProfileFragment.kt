package io.github.horaciocome1.reaque.ui.users.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.FragmentUserProfileBinding
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.layout_user_profile_actions_section.*

class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    private val viewModel: UserProfileViewModel by lazy {
        val factory = InjectorUtils.userProfileViewModelFactory
        ViewModelProviders.of(this, factory)[UserProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val user = User(
                UserProfileFragmentArgs.fromBundle(bundle).userId
            )
            viewModel.get(user)
                .observe(this, Observer {
                    binding.user = it
                })
            viewModel.amSubscribedTo(user)
                .observe(this, Observer {
                    when (it) {
                        Constants.States.TRUE -> {
                            subscribe_button.visibility = View.GONE
                            unsubscribe_button.visibility = View.VISIBLE
                        }
                        Constants.States.FALSE -> {
                            subscribe_button.visibility = View.VISIBLE
                            unsubscribe_button.visibility = View.GONE
                        }
                        else -> {
                            subscribe_button.visibility = View.GONE
                            unsubscribe_button.visibility = View.GONE
                        }
                    }
                })
        }
    }

}