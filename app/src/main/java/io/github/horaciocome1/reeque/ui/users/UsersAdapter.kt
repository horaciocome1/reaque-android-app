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

package io.github.horaciocome1.reeque.ui.users

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.user.User
import jp.wasabeef.picasso.transformations.BlurTransformation

class UsersAdapter(private val context: Context, private val list: List<User>)
    : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].run {
            Picasso.with(context).load(profilePic)
                .transform(BlurTransformation(context, 7, 14)).into(holder.cover)
            Picasso.with(context).load(profilePic).into(holder.profilePic)
            holder.name.text = name
            holder.categories.text = categories
            holder.town.text = town
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val cover: ImageView = view.findViewById(R.id.item_user_cover_imageview)
        val profilePic: CircleImageView = view.findViewById(R.id.item_user_profile_pic_imageview)
        val name: TextView = view.findViewById(R.id.item_user_name_textview)
        val categories: TextView = view.findViewById(R.id.item_user_categories_textview)
        val town: TextView = view.findViewById(R.id.item_user_town_textview)

    }

}