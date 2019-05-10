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
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.FragmentUsersBinding
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment() {

    private lateinit var binding: FragmentUsersBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topics_recyclerview.run {
            addOnItemClickListener { _, position ->
                binding.topics?.let {
                    if (it.isNotEmpty())
                        listUsers(it[position])
                }
            }
        }
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener {
            it.currentUser?.let {
                viewModel.notEmptyTopics.observe(this, Observer { list ->
                    binding.topics = list
                    topics_progressbar.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                })
            }
        }
        favorites_fab.setOnClickListener {
            listFavorites()
        }
    }

    private fun listUsers(topic: Topic) {
        auth.addAuthStateListener {
            it.currentUser?.let {
                viewModel.getUsers(topic).observe(this, Observer { list ->
                    binding.users = list
                    users_progressbar.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                })
                favorites_fab.show()
            }
        }
    }

    private fun listFavorites() {
        auth.addAuthStateListener {
            it.currentUser?.let {
                viewModel.favorites.observe(this, Observer { list ->
                    binding.users = list
                    users_progressbar.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
                })
                favorites_fab.hide()
            }
        }
    }

}