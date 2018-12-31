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
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.topics.Topic
import io.github.horaciocome1.reeque.ui.MainActivity
import io.github.horaciocome1.reeque.ui.users.loadUsers
import io.github.horaciocome1.reeque.utilities.InjectorUtils
import kotlinx.android.synthetic.main.fragment_posts.*

var topic = Topic("")
lateinit var fragmentManager: FragmentManager
const val namePostsFragment = "PostsFragment"

fun FragmentManager.loadPosts(topic: Topic) {
    this.beginTransaction().replace(R.id.activity_main_container, PostsFragment()).addToBackStack(namePostsFragment).commit()
    io.github.horaciocome1.reeque.ui.posts.topic = topic
    fragmentManager = this
}

class PostsFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initialize(view)
    }

    private fun initialize(view: View) {
        fragment_posts_title.text = topic.title
        val factory = InjectorUtils.providePostsViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)
        viewModel.getPosts().observe(this, Observer { fragment_posts_recyclerview.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = PostsAdapter(view.context, it, fragmentManager)
        } })
        fragment_posts_authors_button.setOnClickListener { fragmentManager?.loadUsers(topic) }
    }

}
