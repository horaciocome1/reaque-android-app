package io.github.horaciocome1.reaque.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.databinding.FragmentUsersBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.layout_users_list.*

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding

    private val viewModel: UsersViewModel by lazy {
        val factory = InjectorUtils.usersViewModelFactory
        ViewModelProviders.of(this, factory)[UsersViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        users_recyclerview.addOnItemClickListener(viewModel.onItemClickListener)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val args = UsersFragmentArgs.fromBundle(bundle)
            viewModel.get(args.parentId, args.requestId)
                .observe(this, Observer {
                    binding.viewmodel = viewModel.setUsers(it)
                })
        }
    }

}