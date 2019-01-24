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
import io.github.horaciocome1.reaque.ui.favorites.getFavoritesViewModel
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_posts_list.*

fun FragmentManager.getPostsListFragment(): PostsListFragment {
    fragmentManager = this
    return PostsListFragment()
}

class PostsListFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        var list = listOf<Post>()
        getFavoritesViewModel().getPosts().observe(this, Observer { posts ->
            when {
                posts.isEmpty() -> fragment_list_recyclerview.visibility = View.GONE
                list.isEmpty() -> {
                    list = posts
                    configList(list)
                    fragment_posts_list_recyclerview.visibility = View.VISIBLE
                    fragment_posts_list_progressbar.visibility = View.GONE
                }
                posts != list -> {
                    fragment_posts_list_tap_to_update_button.run {
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

    private fun configList(list: List<Post>) = fragment_posts_list_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = FavoritePostAdapter(context, list)
        setOnClick { _, position -> fragmentManager?.loadPost(list[position]) }
        addSimpleTouchListener()
    }

}