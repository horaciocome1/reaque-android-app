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

package io.github.horaciocome1.reaque.ui.notifications

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.databinding.ItemNotificationBinding

class NotificationsAdapter(private val list: List<Notification>) :
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    private lateinit var binding: ItemNotificationBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        binding = ItemNotificationBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            Glide.with(context)
                .load(cover)
                .into(binding.itemNotificationImageview)
            binding.notification = this

            when {
                topicId != "" -> {

                }
                postId != "" -> {

                }
                userId != "" -> {

                }
            }
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}