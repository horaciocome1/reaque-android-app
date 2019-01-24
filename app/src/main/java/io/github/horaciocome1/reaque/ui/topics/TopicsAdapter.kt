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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.ItemTopicBinding
import io.github.horaciocome1.reaque.ui.posts.loadPost
import io.github.horaciocome1.reaque.ui.topics.details.loadTopic
import io.github.horaciocome1.reaque.ui.users.loadProfile

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
            binding.itemTopicMoreButton.setOnClickListener { fragmentManager?.loadTopic(this) }
            binding.itemTopicPost1TitleTextview.setOnClickListener { fragmentManager?.loadPost(posts[0]) }
            binding.itemTopicPost2TitleTextview.setOnClickListener { fragmentManager?.loadPost(posts[1]) }
            binding.itemTopicPost3TitleTextview.setOnClickListener { fragmentManager?.loadPost(posts[2]) }
            binding.itemTopicPost1ProfilePicImageview.setOnClickListener {
                fragmentManager?.loadProfile(posts[0].user)
            }
            binding.itemTopicPost2ProfilePicImageview.setOnClickListener {
                fragmentManager?.loadProfile(posts[1].user)
            }
            binding.itemTopicPost3ProfilePicImageview.setOnClickListener {
                fragmentManager?.loadProfile(posts[2].user)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}