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
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.users.User
import io.github.horaciocome1.reeque.databinding.FragmentProfileBinding
import io.github.horaciocome1.reeque.ui.fragmentManager
import io.github.horaciocome1.reeque.ui.posts.loadUserPosts
import io.github.horaciocome1.reeque.ui.posts.viewPic
import io.github.horaciocome1.reeque.utilities.InjectorUtils
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile.*

var user = User("")

fun FragmentManager.loadProfile(user: User) {
    this.beginTransaction().replace(R.id.activity_main_container, ProfileFragment())
        .addToBackStack("ProfileFragment").commit()
    fragmentManager = this
    io.github.horaciocome1.reeque.ui.users.user = user
}

class ProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentProfileActionButton.text = "Seguir"
        binding.fragmentProfileActionButton.setOnClickListener { binding.fragmentProfileActionButton.text = "Seguindo" }

        val factory = InjectorUtils.provideUsersViewModelFactory()
        val viewModel = ViewModelProviders.of(this, factory)[UsersViewModel::class.java]
        viewModel.getUsers(user).observe(this, Observer { user ->
            binding.user = user
            Glide.with(this).load(user.pic2)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(14,2)))
                .into(binding.fragmentProfileCoverImageview)
            Glide.with(this).load(user.pic2).into(binding.fragmentProfileProfilePicImageview)
            fragment_profile_more_button.setOnClickListener {
                fragmentManager?.loadUserPosts(user)
            }
            fragment_profile_profile_pic_imageview.setOnClickListener { fragmentManager?.viewPic(user.pic2) }
            fragment_profile_cover_imageview.setOnClickListener { fragmentManager?.viewPic(user.pic2) }
        })
    }

}