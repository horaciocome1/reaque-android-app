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
import io.github.horaciocome1.reeque.databinding.FragmentProfileBinding
import io.github.horaciocome1.reeque.ui.fragmentManager
import io.github.horaciocome1.reeque.ui.posts.loadUserPosts
import io.github.horaciocome1.reeque.ui.posts.viewPic
import io.github.horaciocome1.reeque.utilities.InjectorUtils
import jp.wasabeef.picasso.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile.*

fun FragmentManager.loadMyProfile() {
    this.beginTransaction().replace(R.id.activity_main_container, MyProfileFragment()).commit()
    fragmentManager = this
}

class MyProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentProfileActionButton.text = "Alterar"

        val factory = InjectorUtils.provideUsersViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)[UsersViewModel::class.java]
        viewModel.getUsers("").observe(this, Observer { user ->
            binding.user = user
            Picasso.with(context).load(user.pic).transform(BlurTransformation(context, 2,14))
                .into(binding.fragmentProfileCoverImageview)
            Picasso.with(context).load(user.pic).into(binding.fragmentProfileProfilePicImageview)
            fragment_profile_more_button.setOnClickListener { fragmentManager?.loadUserPosts(user) }
            fragment_profile_profile_pic_imageview.setOnClickListener {
                fragmentManager?.viewPic(io.github.horaciocome1.reeque.ui.users.user.pic)
            }
            fragment_profile_cover_imageview.setOnClickListener {
                fragmentManager?.viewPic(io.github.horaciocome1.reeque.ui.users.user.pic)
            }
        })
    }

}