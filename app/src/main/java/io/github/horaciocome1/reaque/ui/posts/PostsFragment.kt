package io.github.horaciocome1.reaque.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.databinding.FragmentPostsBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.layout_posts_list.*

class PostsFragment : Fragment() {

    private lateinit var binding: FragmentPostsBinding

    private val viewModel: PostsViewModel by lazy {
        val factory = InjectorUtils.postsViewModelFactory
        ViewModelProviders.of(this, factory)[PostsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        posts_recyclerview.addOnItemClickListener(viewModel.onItemClickListener)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val args = PostsFragmentArgs.fromBundle(bundle)
            viewModel.get(args.parentId, args.requestId)
                .observe(this, Observer {
                    binding.viewmodel = viewModel.setPosts(it)
                })
        }
    }

}