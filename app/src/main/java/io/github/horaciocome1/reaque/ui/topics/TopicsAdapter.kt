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

package io.github.horaciocome1.reaque.ui.topics

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.ItemTopicBinding

class TopicsAdapter(private val list: List<Topic>) : RecyclerView.Adapter<TopicsAdapter.ViewHolder>() {

    lateinit var binding: ItemTopicBinding
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemTopicBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            binding.let { b ->
                b.topic = this

                Glide.with(context).run {
                    load(posts[0].user.pic).apply(RequestOptions.circleCropTransform())
                        .into(b.itemTopicPost1ProfilePicImageview)
                    load(posts[1].user.pic).apply(RequestOptions.circleCropTransform())
                        .into(b.itemTopicPost2ProfilePicImageview)
                    load(posts[2].user.pic).apply(RequestOptions.circleCropTransform())
                        .into(b.itemTopicPost3ProfilePicImageview)
                }

                b.itemTopicPost1TitleTextview.setOnClickListener { read(it, posts[0]) }
                b.itemTopicPost2TitleTextview.setOnClickListener { read(it, posts[1]) }
                b.itemTopicPost3TitleTextview.setOnClickListener { read(it, posts[2]) }

                b.itemTopicPost1ProfilePicImageview.setOnClickListener { openProfile(it, posts[0]) }
                b.itemTopicPost2ProfilePicImageview.setOnClickListener { openProfile(it, posts[1]) }
                b.itemTopicPost3ProfilePicImageview.setOnClickListener { openProfile(it, posts[2]) }

                b.itemTopicMoreButton.setOnClickListener {
                    val openTopicAction = TopicsFragmentDirections.actionOpenPosts(id, title)
                    Navigation.findNavController(it).navigate(openTopicAction)
                }

                b.itemTopicWritersButton.setOnClickListener {
                    val openUsersAction = TopicsFragmentDirections.actionOpenUsers(id, title)
                    Navigation.findNavController(it).navigate(openUsersAction)
                }

                b.itemTopicCommentsButton.setOnClickListener {
                    val openCommentsAction = TopicsFragmentDirections.actionOpenComments(id, title)
                    Navigation.findNavController(it).navigate(openCommentsAction)
                }
            }
        }
    }

    private fun openProfile(view: View, post: Post) {
        val openProfileAction = TopicsFragmentDirections.actionOpenProfile(post.user.id)
        Navigation.findNavController(view).navigate(openProfileAction)
    }

    private fun read(view: View, post: Post) {
        val actionRead = TopicsFragmentDirections.actionRead(post.id)
        Navigation.findNavController(view).navigate(actionRead)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}