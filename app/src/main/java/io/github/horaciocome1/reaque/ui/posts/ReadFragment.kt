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
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.FragmentReadBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.imageviewer.viewPic
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import io.github.horaciocome1.reaque.utilities.getGlide
import kotlinx.android.synthetic.main.fragment_read.*

var post = Post("")

fun FragmentManager.loadPost(post: Post) {
    val fragment = ReadFragment()
    beginTransaction().replace(R.id.activity_main_container, fragment)
        .addToBackStack(fragment.tag).commit()
    io.github.horaciocome1.reaque.ui.posts.post = post
    fragmentManager = this
}

class ReadFragment: Fragment() {

    lateinit var binding: FragmentReadBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            val postId = ReadFragmentArgs.fromBundle(it).postId
            getPostsViewModel().getPosts(Post(postId)).observe(this, Observer { posts ->
                when {
                    posts.isEmpty() -> {
//                        fragment_read_appbar.visibility = View.GONE
                        fragment_read_message_scrollview.visibility = View.GONE
                        fragment_read_details_scrollview.visibility = View.GONE
                    }
                    else -> {
                        post = posts[0]
//                        fragment_read_appbar.visibility = View.VISIBLE
                        fragment_read_message_scrollview.visibility = View.VISIBLE
                        fragment_read_details_scrollview.visibility = View.VISIBLE
                        fragment_read_progressbar.visibility = View.GONE
                        (activity as MainActivity).supportActionBar?.title = post.title
                        binding.post = post
                        getGlide().load(post.cover).into(fragment_read_cover)
                        getGlide().load(post.user.pic).into(fragment_read_profile_pic)
                        fragment_read_cover.setOnClickListener { fragmentManager?.viewPic(post.cover) }
                        fragment_read_profile_pic.setOnClickListener { fragmentManager?.viewPic(post.user.pic) }
                    }
                }
            })
        }
    }

}