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

package io.github.horaciocome1.reaque.ui.topics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.ItemTopicBinding

class TopicsAdapter(private val context: Context,
                    private val list: List<Topic>,
                    private val fragmentManager: FragmentManager?)
    : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    lateinit var binding: ItemTopicBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTopicBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            binding.topic = this

            Glide.with(context).load(posts[0].user.pic).into(binding.itemTopicPost1ProfilePicImageview)
            Glide.with(context).load(posts[1].user.pic).into(binding.itemTopicPost2ProfilePicImageview)
            Glide.with(context).load(posts[2].user.pic).into(binding.itemTopicPost3ProfilePicImageview)

            binding.itemTopicPost1TitleTextview.setOnClickListener {
                //                fragmentManager?.loadPost(posts[0])
                read(it, posts[0])
            }
            binding.itemTopicPost2TitleTextview.setOnClickListener {
                //                fragmentManager?.loadPost(posts[1])
                read(it, posts[1])
            }
            binding.itemTopicPost3TitleTextview.setOnClickListener {
                //                fragmentManager?.loadPost(posts[2])
                read(it, posts[2])
            }


            binding.itemTopicPost1ProfilePicImageview.setOnClickListener {
                //                fragmentManager?.loadProfile(posts[0].user)
                openProfile(it, posts[0].user)
            }

            binding.itemTopicPost2ProfilePicImageview.setOnClickListener {
                //                fragmentManager?.loadProfile(posts[1].user)
                openProfile(it, posts[1].user)
            }

            binding.itemTopicPost3ProfilePicImageview.setOnClickListener {
                //                fragmentManager?.loadProfile(posts[2].user)
                openProfile(it, posts[2].user)
            }

            binding.itemTopicMoreButton.setOnClickListener {
                val openTopicAction = TopicsFragmentDirections.actionOpenPosts(this.id, this.title)
                Navigation.findNavController(it).navigate(openTopicAction)
            }

            binding.itemTopicWritersButton.setOnClickListener {
                val openUsersAction = TopicsFragmentDirections.actionOpenUsers(this.id, this.title)
                Navigation.findNavController(it).navigate(openUsersAction)
            }

            binding.itemTopicCommentsButton.setOnClickListener {
                val openCommentsAction = TopicsFragmentDirections.actionOpenComments(this.id, this.title)
                Navigation.findNavController(it).navigate(openCommentsAction)
            }

        }
    }

    private fun openProfile(view: View, user: User) {
        val openProfileAction = TopicsFragmentDirections.actionOpenProfile(user.id)
        Navigation.findNavController(view).navigate(openProfileAction)
    }

    private fun read(view: View, post: Post) {
        val actionRead = TopicsFragmentDirections.actionRead(post.id)
        Navigation.findNavController(view).navigate(actionRead)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}