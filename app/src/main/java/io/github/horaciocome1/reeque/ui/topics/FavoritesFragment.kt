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

package io.github.horaciocome1.reeque.ui.topics

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
import io.github.horaciocome1.reeque.ui.posts.PostsAdapter
import io.github.horaciocome1.reeque.ui.posts.PostsViewModel
import io.github.horaciocome1.reeque.utilities.InjectorUtils
import kotlinx.android.synthetic.main.fragment_favorites.*

fun FragmentManager.loadFavorites() {
    beginTransaction().replace(R.id.activity_main_container, FavoritesFragment()).commit()
    io.github.horaciocome1.reeque.ui.fragmentManager = this
}

class FavoritesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_favorites_topics_tabitem.setOnClickListener {
            val factory = InjectorUtils.provideTopicsViewModelFactory()
            val viewModel = ViewModelProviders.of(this, factory).get(TopicsViewModel::class.java)
            viewModel.getTopics().observe(this, Observer { list ->
                fragment_favorites_recyclerview.apply {
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = TopicsAdapter(view.context, list, fragmentManager)
                }
            })
        }
        fragment_favorites_posts_tabitem.setOnClickListener {
            val factory = InjectorUtils.providePostsViewModelFactory()
            val viewModel = ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)
            viewModel.getPosts().observe(this, Observer { list ->
                fragment_favorites_recyclerview.apply {
                    layoutManager = LinearLayoutManager(view.context)
                    adapter = PostsAdapter(view.context, list, fragmentManager)
                }
            })
        }
    }
}