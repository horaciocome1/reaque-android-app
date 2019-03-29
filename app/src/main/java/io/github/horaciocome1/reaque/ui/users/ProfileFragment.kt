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

package io.github.horaciocome1.reaque.ui.users

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.FragmentProfileBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.posts.ReadFragmentDirections
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { args ->
            val userId = ProfileFragmentArgs.fromBundle(args).userId
            viewModel.getUsers(User(userId)).observe(this, Observer { user ->
                when {
                    user.id.isBlank() -> hideContent()
                    else -> {
                        user.run {
                            binding.user = this
                            Glide.with(this@ProfileFragment).load(pic).run {
                                apply(RequestOptions.bitmapTransform(BlurTransformation(7, 14)))
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(fragment_profile_cover_imageview)
                                apply(RequestOptions.circleCropTransform())
                                    .transition(DrawableTransitionOptions.withCrossFade())
                                    .into(fragment_profile_profile_pic_imageview)
                            }
                            fragment_profile_profile_pic_imageview.setOnClickListener {
                                openViewer(it, pic)
                            }
                        }
                        showContent()
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            (activity as MainActivity).supportActionBar?.run {
                show()
                title = ""
            }
        }
    }

    private fun showContent() {
        fragment_profile_cover_imageview.visibility = View.VISIBLE
        fragment_profile_scrollview.visibility = View.VISIBLE
        fragment_profile_progressbar.visibility = View.GONE
    }

    private fun hideContent() {
        fragment_profile_cover_imageview.visibility = View.GONE
        fragment_profile_scrollview.visibility = View.GONE
        fragment_profile_progressbar.visibility = View.VISIBLE
    }

    private fun openViewer(view: View, url: String) {
        val directions = ReadFragmentDirections.actionOpenViewerFromRead(url)
        view.findNavController().navigate(directions)
    }

}