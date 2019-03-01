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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment: Fragment() {

    private var list = listOf<Post>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { bundle ->
            val safeArgs = PostsFragmentArgs.fromBundle(bundle)
            safeArgs.run {
                if (isFromTopic)
                    viewModel.getPosts(Topic(id)).observe(this@PostsFragment, Observer {
                        configPosts(it)
                        (activity as MainActivity).supportActionBar?.title = title
                    })
                else if (isFromUser)
                    viewModel.getPosts(User(id)).observe(this@PostsFragment, Observer { configPosts(it) })
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.show()
    }

    private fun configList(list: List<Post>) = fragment_posts_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = PostsAdapter(list)
        setOnClick { view, position ->
            val openRead = PostsFragmentDirections.actionOpenReadFromPosts(list[position].id)
            Navigation.findNavController(view).navigate(openRead)
        }
        addSimpleTouchListener()
    }

    private fun configPosts(posts: List<Post>) {
        when {
            posts.isEmpty() -> fragment_posts_progressbar.visibility = View.VISIBLE
            list.isEmpty() -> {
                list = posts
                configList(list)
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
    }

}
