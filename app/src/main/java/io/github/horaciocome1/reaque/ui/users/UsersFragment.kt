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

package io.github.horaciocome1.reaque.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.topics.TopicsAdapter
import io.github.horaciocome1.reaque.utilities.Constants
import io.github.horaciocome1.reaque.utilities.isPortrait
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    private var topics = listOf<Topic>()
    private var users = listOf<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topics_recyclerview.run {
            addOnItemClickListener { _, position ->
                if (topics.isNotEmpty()) {
                    favorites_fab.show()
                    topics[position].listUsers()
                }
            }
        }
        users_recyclerview.run {
            addOnItemClickListener { view, position ->
                if (users.isNotEmpty())
                    users[position].openProfile(view)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.topics.observe(this, Observer {
            topics = it
            topics_recyclerview.run {
                layoutManager = if (isPortrait) LinearLayoutManager(
                    context,
                    RecyclerView.HORIZONTAL,
                    false
                ) else LinearLayoutManager(context)
                adapter = TopicsAdapter(topics)
            }
            topics_progressbar.visibility = if (topics.isEmpty()) View.VISIBLE else View.GONE
        })
        favorites_fab.setOnClickListener {
            viewModel.favorites.observe(this, Observer {
                users_recyclerview.setupWithUsers(it)
            })
            favorites_fab.hide()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPortrait)
            (activity as MainActivity).supportActionBar?.hide()
        users_progressbar.visibility = View.GONE
    }

    private fun Topic.listUsers() {
        viewModel.getUsers(this).observe(this@UsersFragment, Observer {
            users_recyclerview.setupWithUsers(it)
        })
        favorites_fab.show()
    }

    private fun RecyclerView.setupWithUsers(list: List<User>) {
        users = list
        layoutManager = StaggeredGridLayoutManager(
            when {
                users.size >= Constants.TWO_COLUMNS -> Constants.TWO_COLUMNS
                else -> Constants.SINGLE_COLUMN
            },
            RecyclerView.VERTICAL
        )
        adapter = UsersAdapter(users)
        users_progressbar.visibility = if (users.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun User.openProfile(view: View) {
        val directions = UsersFragmentDirections.actionOpenProfileFromUsers(id)
        view.findNavController().navigate(directions)
    }

}