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
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    private val columns = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            val safeArgs = UsersFragmentArgs.fromBundle(it)
            val topic = Topic(safeArgs.topicId)
            (activity as MainActivity).supportActionBar?.title = safeArgs.topicTitle
            var list = listOf<User>()
            getUsersViewModel().getUsers(topic).observe(this, Observer { users ->
                when {
                    users.isEmpty() -> fragment_users_recyclerview.visibility = View.GONE
                    list.isEmpty() -> {
                        list = users
                        configList(list)
                        fragment_users_recyclerview.visibility = View.VISIBLE
                        fragment_users_progressbar.visibility = View.GONE
                    }
                    users != list -> {
                        fragment_users_tap_to_update_button.run {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                list = users
                                configList(list)
                                visibility = View.GONE
                            }
                        }
                    }
                }
            })
        }
    }

    private fun configList(list: List<User>) = fragment_users_recyclerview.apply {
        layoutManager = StaggeredGridLayoutManager(columns, RecyclerView.VERTICAL)
        adapter = UsersAdapter(context, list)
        setOnClick { view, position ->
            val openProfile = UsersFragmentDirections.actionOpenProfile(list[position].id)
            Navigation.findNavController(view).navigate(openProfile)
        }
        addSimpleTouchListener()
    }

}