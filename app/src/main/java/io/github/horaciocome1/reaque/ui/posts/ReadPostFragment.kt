package io.github.horaciocome1.reaque.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.FragmentReadPostBinding
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_read_post.*

class ReadPostFragment : Fragment() {

    lateinit var binding: FragmentReadPostBinding

    private val viewModel: PostsViewModel by lazy {
        val factory = InjectorUtils.postsViewModelFactory
        ViewModelProviders.of(this, factory)[PostsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReadPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        share_button?.setOnClickListener {
            binding.post?.let { viewModel.share(this, view, it) }
        }
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val post = Post(
                ReadPostFragmentArgs.fromBundle(bundle).postId
            )
            viewModel.get(post).observe(this, Observer {
                binding.post = it
                viewModel.readingPost = it
            })
            viewModel.getRating(post).observe(this, Observer { rating_button?.text = it.toString() })
            viewModel.isBookmarked(post).observe(this, Observer {
                when (it) {
                    Constants.States.TRUE -> {
                        bookmark_button.visibility = View.GONE
                        unbookmark_button.visibility = View.VISIBLE
                    }
                    Constants.States.FALSE -> {
                        bookmark_button.visibility = View.VISIBLE
                        unbookmark_button.visibility = View.GONE
                    }
                    else -> {
                        bookmark_button.visibility = View.GONE
                        unbookmark_button.visibility = View.GONE
                    }
                }
            })
        }
    }

}