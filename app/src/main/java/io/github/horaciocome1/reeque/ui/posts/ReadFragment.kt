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
import com.squareup.picasso.Picasso
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.posts.Post
import io.github.horaciocome1.reeque.databinding.FragmentReadBinding
import io.github.horaciocome1.reeque.ui.MainActivity
import io.github.horaciocome1.reeque.utilities.InjectorUtils

var post = Post("")
const val nameReadFragment = "ReadFragment"

fun FragmentManager.loadPost(post: Post) {
    this.beginTransaction().replace(R.id.activity_main_container, ReadFragment()).addToBackStack(nameReadFragment).commit()
    io.github.horaciocome1.reeque.ui.posts.post = post
}


class ReadFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentReadBinding.inflate(inflater, container, false)
        val factory = InjectorUtils.providePostsViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory).get(PostsViewModel::class.java)
        viewModel.getPost(post.key).observe(this, Observer {
            binding.post = it
            Picasso.with(binding.root.context).load(it.cover).into(binding.fragmentReadCover)
            Picasso.with(binding.root.context).load(it.profilePic).into(binding.fragmentReadProfilePic)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
    }


}