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

package io.github.horaciocome1.reaque.ui.posts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.ItemPostBinding
import io.github.horaciocome1.reaque.ui.topics.TopicsFragmentDirections
import io.github.horaciocome1.reaque.utilities.getItemPostTransformation

class PostsAdapter(private val context: Context,
                   private val list: List<Post>
)
    : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    private lateinit var binding: ItemPostBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPostBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            binding.post = this
            Glide.with(context).run {
                load(user.pic).apply(RequestOptions.circleCropTransform()).into(binding.itemPostProfileImageview)
                load(cover).apply(getItemPostTransformation()).into(binding.itemPostCoverImageview)
            }
            binding.itemPostReadMoreButton.setOnClickListener {
                val read = TopicsFragmentDirections.actionRead(id)
                Navigation.findNavController(it).navigate(read)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}