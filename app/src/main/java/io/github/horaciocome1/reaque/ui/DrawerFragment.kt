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

package io.github.horaciocome1.reaque.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.horaciocome1.reaque.databinding.FragmentDrawerBinding
import io.github.horaciocome1.reaque.ui.favorites.loadFavorites
import io.github.horaciocome1.reaque.ui.topics.loadTopics
import io.github.horaciocome1.reaque.ui.users.getUsersViewModel
import io.github.horaciocome1.reaque.ui.users.loadMyProfile
import io.github.horaciocome1.reaque.ui.users.user
import io.github.horaciocome1.reaque.utilities.getGlide
import kotlinx.android.synthetic.main.fragment_drawer.*

lateinit var fragmentManager: FragmentManager

fun FragmentManager.loadDrawer(): Boolean {
    val fragment = DrawerFragment()
    fragment.show(this, fragment.tag)
    fragmentManager = this
    return true
}

class DrawerFragment: BottomSheetDialogFragment() {

    lateinit var binding: FragmentDrawerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_drawer_topics_constraintLayout.setOnClickListener {
            dismiss()
            fragmentManager?.loadTopics()
        }
        fragment_drawer_favorites_constraintLayout.setOnClickListener {
            dismiss()
            fragmentManager?.loadFavorites()
        }
        fragment_drawer_settings_constraintLayout.setOnClickListener {
            dismiss()
        }
        fragment_drawer_about_constraintLayout.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        getUsersViewModel().getUsers().observe(this, Observer { users ->
            if (users.isNotEmpty()) {
                user = users[0]
                binding.user = user
                getGlide().load(user.pic).into(binding.fragmentDrawerProfilePicImageview)
                fragment_drawer_profile_pic_imageview.setOnClickListener {
                    dismiss()
                    fragmentManager?.loadMyProfile()
                }
            }
        })
    }

}