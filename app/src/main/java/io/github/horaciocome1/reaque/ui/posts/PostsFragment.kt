/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.FragmentPostsBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.util.isPortrait
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment: Fragment() {

    private lateinit var binding: FragmentPostsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topics_recyclerview.addOnItemClickListener { _, position ->
            binding.topics?.let {
                if (it.isNotEmpty())
                    listPosts(it[position])
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.topics.observe(this, Observer {
            binding.topics = it
            topics_progressbar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
        favorites_fab.setOnClickListener {
            listFavorites()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPortrait)
            activity?.run {
                if (this is MainActivity)
                    supportActionBar?.hide()
            }
    }

    private fun listPosts(topic: Topic) {
        viewModel.getPosts(topic).observe(this, Observer {
            binding.posts = it
            posts_progressbar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
        favorites_fab.show()
    }

    private fun listFavorites() {
        viewModel.favorites.observe(this, Observer {
            binding.posts = it
            posts_progressbar.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
        favorites_fab.hide()
    }

}