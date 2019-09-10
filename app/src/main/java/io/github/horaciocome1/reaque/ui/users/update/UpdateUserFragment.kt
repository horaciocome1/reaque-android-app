package io.github.horaciocome1.reaque.ui.users.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.FragmentUpdateUserBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.reaque.util.OnFocusChangeListener
import kotlinx.android.synthetic.main.fragment_set_rating.*
import kotlinx.android.synthetic.main.fragment_update_user.*
import kotlinx.android.synthetic.main.layout_appbar.*
import kotlinx.android.synthetic.main.layout_update_user_content.*

class UpdateUserFragment
    : Fragment(),
    View.OnClickListener {

    private lateinit var binding: FragmentUpdateUserBinding

    private val viewModel: UpdateUserViewModel by lazy {
        val factory = InjectorUtils.updateUserViewModelFactory
        ViewModelProviders.of(this, factory)[UpdateUserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        appbar_title_textview?.text = getString(R.string.update)
        OnFocusChangeListener(context).let {
            bio_inputlayout.editText?.onFocusChangeListener = it
            address_inputlayout.editText?.onFocusChangeListener = it
        }
        toolbar.setNavigationOnClickListener(viewModel.navigateUp)
        update_button.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val user = User(
                UpdateUserFragmentArgs.fromBundle(bundle).userId
            )
            viewModel.get(user)
                .observe(this, Observer {
                    viewModel.bio.value = it.bio
                    viewModel.address.value = it.address
                    binding.viewmodel = viewModel
                })
        }
        viewModel.bio
            .observe(this, Observer {
                viewModel.user.bio = it
                update_button.isEnabled = viewModel.isUserReady
            })
        viewModel.address
            .observe(this, Observer {
                viewModel.user.address = it
                update_button.isEnabled = viewModel.isUserReady
            })
    }

    override fun onClick(view: View?) {
        if (
            view == done_button
            && view != null
        )
            binding.viewmodel = viewModel.update(view)
    }

}