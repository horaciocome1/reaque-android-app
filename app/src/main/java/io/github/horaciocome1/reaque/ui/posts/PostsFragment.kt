/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
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
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import kotlinx.android.synthetic.main.fragment_posts.*

var topic = Topic("")

fun FragmentManager.getPosts(topic: Topic): PostsFragment {
    io.github.horaciocome1.reaque.ui.posts.topic = topic
    fragmentManager = this
    return PostsFragment()
}

class PostsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            val safeArgs = PostsFragmentArgs.fromBundle(it)
            val topic = Topic(safeArgs.topicId)
            (activity as MainActivity).supportActionBar?.title = safeArgs.topicTitle
            var list = listOf<Post>()
            getPostsViewModel().getPosts(topic).observe(this, Observer { posts ->
                when {
                    posts.isEmpty() -> fragment_posts_recyclerview.visibility = View.INVISIBLE
                    list.isEmpty() -> {
                        list = posts
                        configList(list)
                        fragment_posts_recyclerview.visibility = View.VISIBLE
                        fragment_posts_progressbar.visibility = View.GONE
                    }
                    posts != list -> {
                        fragment_posts_tap_to_update_button.run {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                list = posts
                                configList(list)
                                visibility = View.GONE
                            }
                        }
                    }
                }
            })
        }
    }

    private fun configList(list: List<Post>) = fragment_posts_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = PostsAdapter(context, list, fragmentManager)
    }
}
