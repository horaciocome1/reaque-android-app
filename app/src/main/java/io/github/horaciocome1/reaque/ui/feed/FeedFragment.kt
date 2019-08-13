package io.github.horaciocome1.reaque.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.databinding.FragmentFeedBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_feed.*

class FeedFragment : Fragment() {

    lateinit var binding: FragmentFeedBinding

    private val viewModel: FeedViewModel by lazy {
        val factory = InjectorUtils.feedViewModelFactory
        ViewModelProviders.of(this, factory)[FeedViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        posts_recyclerview.addOnItemClickListener(viewModel.onItemClickListener)
    }

    override fun onStart() {
        super.onStart()
        viewModel.get().observe(this, Observer {
            binding.viewmodel = viewModel.setPosts(it)
        })
    }

}