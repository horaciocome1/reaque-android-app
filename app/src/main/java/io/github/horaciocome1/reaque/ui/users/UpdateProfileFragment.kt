package io.github.horaciocome1.reaque.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.github.horaciocome1.reaque.databinding.FragmentUpdateProfileBinding
import io.github.horaciocome1.reaque.util.OnFocusChangeListener
import kotlinx.android.synthetic.main.fragment_update_user_profile.*

class UpdateProfileFragment : Fragment() {

    private lateinit var binding: FragmentUpdateProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.let {
            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }
        OnFocusChangeListener(context).let {
            bio_inputlayout.editText?.onFocusChangeListener = it
            address_inputlayout.editText?.onFocusChangeListener = it
        }
        toolbar?.setNavigationOnClickListener {
            viewModel.navigateUp(it)
        }
        submit_button.setOnClickListener {
            binding.viewmodel = viewModel.submitProfileUpdates(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.me.observe(this, Observer {
            viewModel.run {
                bio.value = it.bio
                address.value = it.address
                binding.viewmodel = this
            }
        })
        viewModel.bio.observe(this, Observer {
            viewModel.user.bio = it
            submit_button.isEnabled = isProfileReady
        })
        viewModel.address.observe(this, Observer {
            viewModel.user.address = it
            submit_button.isEnabled = isProfileReady
        })
    }

    private val isProfileReady: Boolean
        get() {
            viewModel.user.run {
                return bio.isNotBlank() && address.isNotBlank() && bio != "null" && address != "null"
            }
        }

}