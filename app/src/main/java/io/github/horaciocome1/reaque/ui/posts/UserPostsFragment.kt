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
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import io.github.horaciocome1.reaque.ui.users.user
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_user_posts.*

fun FragmentManager.loadUserPosts(user: User) {
    val fragment = UserPostsFragment()
    beginTransaction().replace(R.id.activity_main_container, fragment)
        .addToBackStack(fragment.tag).commit()
    fragmentManager = this
    io.github.horaciocome1.reaque.ui.users.user = user
}

class UserPostsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_user_posts_back_button.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onStart() {
        super.onStart()
        var list = listOf<Post>()
        getPostsViewModel().getPosts(user).observe(this, Observer { posts ->
            when {
                posts.isEmpty() -> fragment_user_posts_recyclerview.visibility = View.GONE
                list.isEmpty() -> {
                    list = posts
                    configList(list)
                    fragment_user_posts_recyclerview.visibility = View.VISIBLE
                }
                posts != list -> {
                    fragment_list_tap_to_update_button.run {
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

    private fun configList(list: List<Post>) = fragment_user_posts_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = PostsAdapter(context, list, fragmentManager)
        setOnClick { _, position -> fragmentManager?.loadPost(list[position]) }
        addSimpleTouchListener()
    }

}