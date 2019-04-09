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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.FragmentReadBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.utilities.isPortrait
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment: Fragment() {

    private lateinit var binding: FragmentReadBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(fragment_read_bottom_sheet)
        fragment_read_cover_imageview.setOnClickListener {
            binding.post?.pic?.run {
                if (isNotBlank())
                    openViewer(it)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.viewmodel = viewModel
        arguments?.let { bundle ->
            val post = Post(ReadFragmentArgs.fromBundle(bundle).postId)
            viewModel.getPosts(post).observe(this, Observer {
                binding.post = it
                Glide.with(this@ReadFragment).run {
                    load(it.pic)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(fragment_read_cover_imageview)
                    load(it.pic)
                        .apply(RequestOptions.bitmapTransform(BlurTransformation(2, 14)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(fragment_read_cover2_imageview)
                    load(it.user.pic)
                        .apply(RequestOptions.circleCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageview)
                }
                behavior.state =
                    if (it.id.isBlank()) BottomSheetBehavior.STATE_HIDDEN else BottomSheetBehavior.STATE_COLLAPSED
            })
        }
        viewModel.isAddingToFavorites.observe(this, Observer {
            add_to_favorite_button.isEnabled = !it
        })
    }

    override fun onResume() {
        super.onResume()
        if (isPortrait)
            (activity as MainActivity).supportActionBar?.show()
    }

    private fun String.openViewer(view: View) {
        val directions = ReadFragmentDirections.actionOpenViewerFromRead(this)
        view.findNavController().navigate(directions)
    }

}