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

package io.github.horaciocome1.reeque.ui.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.topics.Topic
import io.github.horaciocome1.reeque.data.users.User
import io.github.horaciocome1.reeque.ui.fragmentManager
import io.github.horaciocome1.reeque.ui.users.user
import io.github.horaciocome1.reeque.utilities.InjectorUtils
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick

fun FragmentManager.getListOfPosts(topic: Topic, user: User): Fragment {
    fragmentManager = this
    io.github.horaciocome1.reeque.ui.users.user = user
    io.github.horaciocome1.reeque.ui.posts.topic = topic
    return PostsListFragment()
}

class PostsListFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = InjectorUtils.providePostsViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)[PostsViewModel::class.java]
        viewModel.getPosts(io.github.horaciocome1.reeque.ui.users.topic, user).observe(this, Observer {
            if (view is RecyclerView) view.apply {
                layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                adapter = UserPostsAdapter(context, it)
                setOnClick { _, position -> fragmentManager?.loadPost(it[position]) }
                addSimpleTouchListener()
            }
        })
    }
}