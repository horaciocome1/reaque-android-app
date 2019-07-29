package io.github.horaciocome1.reaque.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.databinding.FragmentExploreBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemLongPressListener
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment() {

    lateinit var binding: FragmentExploreBinding

    private val viewModel: ExploreViewModel by lazy {
        val factory = InjectorUtils.exploreViewModelFactory
        ViewModelProviders.of(this, factory)[ExploreViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        posts_recyclerview.addOnItemClickListener(viewModel.onItemPostClickListener)
        topics_recyclerview.addOnItemClickListener(viewModel.onItemTopicClickListener)
        topics_recyclerview.addOnItemLongPressListener(viewModel.onItemTopicLongPressListener)
    }

    override fun onStart() {
        super.onStart()
        viewModel.notEmptyTopics.observe(this, Observer { binding.viewmodel = viewModel.setTopics(it) })
        viewModel.top10.observe(this, Observer { binding.viewmodel = viewModel.setPosts(it) })
    }

}