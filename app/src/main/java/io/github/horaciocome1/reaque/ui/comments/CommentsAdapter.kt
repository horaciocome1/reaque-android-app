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

package io.github.horaciocome1.reaque.ui.comments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.comments.Comment
import io.github.horaciocome1.reaque.databinding.ItemCommentBinding

class CommentsAdapter(private val list: List<Comment>) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var binding: ItemCommentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemCommentBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            binding.comment = this
            Glide.with(context)
                .load(user.pic)
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemCommenWriterPicImageview)
            binding.itemCommenWriterPicImageview.setOnClickListener {
                val openProfile = CommentsFragmentDirections.actionOpenProfile2(user.id)
                Navigation.findNavController(it).navigate(openProfile)
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}