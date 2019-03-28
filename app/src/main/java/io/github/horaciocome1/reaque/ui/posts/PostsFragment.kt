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
import androidx.navigation.Navigation
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.topics.observe(this, Observer {
            if (it.isNotEmpty()) {
                topics_recyclerview.run {
                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    adapter = TopicsAdapter(it)
                    setOnClick { _, position ->
                        favorites_fab.show()
                        it[position].listPosts()
                    }
                    addSimpleTouchListener()
                }
                topics_progressbar.visibility = View.GONE
            } else
                topics_progressbar.visibility = View.VISIBLE
        })
        favorites_fab.setOnClickListener {
            viewModel.favorites.observe(this, Observer {
                posts_recyclerview.setupWithPosts(it)
                if (it.isNotEmpty()) {
                    posts_progressbar.visibility = View.GONE
                    posts_recyclerview.visibility = View.VISIBLE
                } else {
                    posts_progressbar.visibility = View.VISIBLE
                    posts_recyclerview.visibility = View.GONE
                    favorites_fab.hide()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.hide()
        posts_progressbar.visibility = View.GONE
    }

    private fun Topic.listPosts() {
        viewModel.getPosts(this).observe(this@PostsFragment, Observer {
            posts_recyclerview.setupWithPosts(it)
            if (it.isNotEmpty()) {
                posts_progressbar.visibility = View.GONE
                posts_recyclerview.visibility = View.VISIBLE
            } else {
                posts_progressbar.visibility = View.VISIBLE
                posts_recyclerview.visibility = View.GONE
            }
        })
    }

    private fun RecyclerView.setupWithPosts(list: List<Post>) {
        if (list.isNotEmpty()) {
            layoutManager = LinearLayoutManager(context)
            adapter = PostsAdapter(list)
            setOnClick { view, position ->
                list[position].read(view)
            }
            addSimpleTouchListener()
        }
    }

    private fun Post.read(view: View) {
        val directions = PostsFragmentDirections.actionOpenReadFromPosts(id)
        Navigation.findNavController(view).navigate(directions)
    }

}