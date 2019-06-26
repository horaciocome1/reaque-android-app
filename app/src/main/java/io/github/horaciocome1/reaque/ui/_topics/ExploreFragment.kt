package io.github.horaciocome1.reaque.ui._topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.databinding.FragmentExploreBinding
import io.github.horaciocome1.reaque.util.InjectorUtils

class ExploreFragment : Fragment() {

    lateinit var binding: FragmentExploreBinding

    val viewModel: ExploreViewModel by lazy {
        val factory = InjectorUtils.exploreViewModelFactory
        ViewModelProviders.of(this, factory)[ExploreViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.notEmptyTopics.observe(this, Observer { binding.topics = it })
        viewModel.top20.observe(this, Observer { binding.posts = it })
    }

}