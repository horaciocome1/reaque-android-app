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

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.FragmentPostingBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.utilities.Constants
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_posting.*

class PostingFragment : Fragment() {

    private lateinit var selectTopicBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var selectPicBehavior: BottomSheetBehavior<MaterialCardView>

    private lateinit var binding: FragmentPostingBinding

    private var topics = listOf<Topic>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        post_button.isEnabled = false
        select_pic_from_gallery_button.setOnClickListener {
            picImageFromGallery()
        }
        selectTopicBehavior = BottomSheetBehavior.from(select_topics_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
        selectPicBehavior = BottomSheetBehavior.from(select_pic_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
        topics_recyclerview.addOnItemClickListener { _, position ->
            if (topics.isNotEmpty()) {
                selectTopicBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                viewModel.post.topic = topics[position]
                binding.viewmodel = viewModel
                post_button.isEnabled = isPostReady
            }
        }
        select_topic_button.setOnClickListener {
            selectTopicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        select_pic_button.setOnClickListener {
            viewModel.let {
                if (it.imageUri != Uri.EMPTY)
                    Glide.with(this)
                        .load(it.imageUri)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageview)
            }
            selectPicBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        }
    }

    override fun onStart() {
        super.onStart()
        binding.let {
            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }
        viewModel.postTitle.observe(this, Observer {
            viewModel.post.title = it
            post_button.isEnabled = isPostReady
        })
        viewModel.postMessage.observe(this, Observer {
            viewModel.post.message = it
            post_button.isEnabled = isPostReady
        })
        viewModel.topics.observe(this, Observer {
            topics = it
            topics_recyclerview.run {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = SimpleTopicsAdapter(topics)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.hide()
    }

    private val isPostReady: Boolean
        get() {
            viewModel.post.run {
                return (title.isNotBlank() && message.isNotBlank() && topic.id.isNotBlank() && viewModel.imageUri != Uri.EMPTY)
            }
        }

    private fun picImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        }
        startActivityForResult(intent, Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE -> {
                        viewModel.imageUri = data?.data!!
                        Glide.with(this).load(viewModel.imageUri).into(imageview)
                    }
                }
            }
        }
    }

}