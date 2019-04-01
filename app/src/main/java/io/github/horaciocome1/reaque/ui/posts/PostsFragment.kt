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

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment: Fragment() {

    private var posts = listOf<Post>()
    private var topics = listOf<Topic>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topics_recyclerview.run {
            setOnClick { _, position ->
                if (topics.isNotEmpty()) {
                    favorites_fab.show()
                    topics[position].listPosts()
                }
            }
            addSimpleTouchListener()
        }
        posts_recyclerview.run {
            setOnClick { view, position ->
                if (posts.isNotEmpty())
                    posts[position].read(view)
            }
            addSimpleTouchListener()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.topics.observe(this, Observer {
            topics = it
            topics_recyclerview.run {
                layoutManager = when (resources.configuration.orientation) {
                    Configuration.ORIENTATION_LANDSCAPE -> LinearLayoutManager(context)
                    else -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                }
                adapter = TopicsAdapter(topics)
            }
            topics_progressbar.visibility = if (topics.isEmpty()) View.VISIBLE else View.GONE
        })
        favorites_fab.setOnClickListener {
            viewModel.favorites.observe(this, Observer {
                posts_recyclerview.setupWithPosts(it)
            })
            favorites_fab.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
        posts_progressbar.visibility = View.GONE
    }

    private fun Topic.listPosts() {
        viewModel.getPosts(this).observe(this@PostsFragment, Observer {
            posts_recyclerview.setupWithPosts(it)
        })
        favorites_fab.show()
    }

    private fun RecyclerView.setupWithPosts(list: List<Post>) {
        posts = list
        layoutManager = LinearLayoutManager(context)
        adapter = PostsAdapter(posts)
        posts_progressbar.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
        posts_recyclerview.visibility = if (posts.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun Post.read(view: View) {
        val directions = PostsFragmentDirections.actionOpenReadFromPosts(id)
        view.findNavController().navigate(directions)
    }

}