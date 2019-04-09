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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.FragmentProfileBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.posts.PostsAdapter
import io.github.horaciocome1.reaque.utilities.isPortrait
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private var posts = listOf<Post>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(posts_bottomsheet)
        posts_recyclerview.run {
            setOnClick { view, position ->
                if (posts.isNotEmpty())
                    posts[position].read(view)
            }
            addSimpleTouchListener()
        }
        imageview.setOnClickListener {
            binding.user?.pic?.run {
                if (isNotBlank())
                    openViewer(it, this)
            }
        }
        posts_button.setOnClickListener {
            binding.user?.run {
                if (id.isNotBlank())
                    showPosts()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        arguments?.let { args ->
            val user = User(ProfileFragmentArgs.fromBundle(args).userId)
            viewModel.getUsers(user).observe(this, Observer {
                binding.user = it
                Glide.with(this@ProfileFragment).load(it.pic).run {
                    apply(RequestOptions.bitmapTransform(BlurTransformation(7, 14)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(cover_imageview)
                    apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageview)
                }
                if (it.id.isBlank())
                    hideContent()
                else
                    showContent()
            })
        }
        viewModel.isAddingToFavorites.observe(this, Observer {
            add_to_favorite_button.isEnabled = !it
        })
    }

    override fun onResume() {
        super.onResume()
        if (isPortrait) {
            (activity as MainActivity).supportActionBar?.run {
                show()
                title = ""
            }
        }
    }

    private fun showContent() {
        cover_imageview.visibility = View.VISIBLE
        scrollview.visibility = View.VISIBLE
        progressbar.visibility = View.GONE
    }

    private fun hideContent() {
        cover_imageview.visibility = View.GONE
        scrollview.visibility = View.GONE
        progressbar.visibility = View.VISIBLE
    }

    private fun openViewer(view: View, url: String) {
        val directions = ProfileFragmentDirections.actionOpenViewerFromProfile(url)
        view.findNavController().navigate(directions)
    }

    private fun User.showPosts() {
        viewModel.getPosts(this).observe(this@ProfileFragment, Observer {
            posts = it
            posts_recyclerview.run {
                layoutManager = LinearLayoutManager(context)
                adapter = PostsAdapter(posts)
            }
            posts_progressbar.visibility = if (posts.isEmpty()) View.VISIBLE else View.GONE
            behavior.run {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                skipCollapsed = true
            }
        })
    }

    private fun Post.read(view: View) {
        val directions = ProfileFragmentDirections.actionOpenReadFromProfile(id)
        Navigation.findNavController(view).navigate(directions)
    }

}