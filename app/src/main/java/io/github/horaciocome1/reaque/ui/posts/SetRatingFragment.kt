package io.github.horaciocome1.reaque.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.databinding.FragmentSetRatingBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_set_rating.*

class SetRatingFragment : Fragment() {

    lateinit var binding: FragmentSetRatingBinding

    private val viewModel: PostsViewModel by lazy {
        val factory = InjectorUtils.postsViewModelFactory
        ViewModelProviders.of(this, factory)[PostsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSetRatingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        toolbar?.setNavigationOnClickListener { viewModel.navigateUp(it) }
    }

}