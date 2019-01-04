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
import com.google.android.material.tabs.TabLayout
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.users.User
import io.github.horaciocome1.reeque.ui.MainActivity
import io.github.horaciocome1.reeque.ui.fragmentManager
import io.github.horaciocome1.reeque.ui.topics.TopicsViewModel
import io.github.horaciocome1.reeque.ui.users.TabAdapter
import io.github.horaciocome1.reeque.ui.users.user
import io.github.horaciocome1.reeque.utilities.InjectorUtils
import kotlinx.android.synthetic.main.fragment_user_posts.*

fun FragmentManager.loadUserPosts(user: User) {
    beginTransaction().replace(R.id.activity_main_container, UserPostsFragment())
        .addToBackStack("UserPostsFragment").commit()
    fragmentManager = this
    io.github.horaciocome1.reeque.ui.users.user = user
}

class UserPostsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        var tabAdapter: TabAdapter
        val factory = InjectorUtils.provideTopicsViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)[TopicsViewModel::class.java]
        viewModel.getTopics().observe(this, Observer {
            tabAdapter = TabAdapter(fragmentManager)
            it.forEach { topic ->
                tabAdapter.addFragment(topic.title, fragmentManager?.getListOfPosts(topic, user)
            ) }

            if (tabAdapter.count <= 3) fragment_user_posts_tablayout.tabMode = TabLayout.MODE_FIXED
            else fragment_user_posts_tablayout.tabMode = TabLayout.MODE_SCROLLABLE

            fragment_user_posts_viewpager.adapter = tabAdapter
            fragment_user_posts_tablayout.setupWithViewPager(fragment_user_posts_viewpager)
        })
    }

}