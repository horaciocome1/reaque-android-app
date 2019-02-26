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

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.FragmentReadBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment: Fragment() {

    private lateinit var binding: FragmentReadBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        arguments?.let { args ->
            val postId = ReadFragmentArgs.fromBundle(args).postId
            viewModel.getPosts(Post(postId)).observe(this, Observer {
                if (it.id == "")
                    fragment_read_bottom_sheet.visibility = View.GONE
                else {
                    fragment_read_bottom_sheet.visibility = View.VISIBLE
                    val behavior = BottomSheetBehavior.from(fragment_read_bottom_sheet)
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    it.run {
                        binding.post = this
                        Glide.with(this@ReadFragment).run {
                            load(cover)
                                .into(fragment_read_cover_imageview)
                            load(cover)
                                .apply(RequestOptions.bitmapTransform(BlurTransformation(2, 14)))
                                .into(fragment_read_cover2_imageview)
                            load(user.pic)
                                .apply(RequestOptions.circleCropTransform())
                                .into(fragment_read_profile_pic_imageview)
                        }
                    }
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.show()
    }

}