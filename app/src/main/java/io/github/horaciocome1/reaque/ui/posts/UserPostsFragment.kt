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
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_user_posts.*

class UserPostsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_posts, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { args ->
            val userId = UserPostsFragmentArgs.fromBundle(args).userId
            val userName = UserPostsFragmentArgs.fromBundle(args).userName
            var list = listOf<Post>()
            (activity as MainActivity).supportActionBar?.title = userName
            getPostsViewModel().getPosts(User(userId)).observe(this, Observer { posts ->
                when {
                    posts.isEmpty() -> fragment_user_posts_recyclerview.visibility = View.GONE
                    list.isEmpty() -> {
                        list = posts
                        configList(list)
                        fragment_user_posts_recyclerview.visibility = View.VISIBLE
                        fragment_user_posts_progressbar.visibility = View.GONE
                    }
                    posts != list -> {
                        fragment_user_posts_tap_to_update_button.run {
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

    private fun configList(list: List<Post>) = fragment_user_posts_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = PostsAdapter(context, list)
    }

}