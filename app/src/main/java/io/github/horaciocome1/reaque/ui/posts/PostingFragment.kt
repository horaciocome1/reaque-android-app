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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import io.github.horaciocome1.reaque.databinding.FragmentPostingBinding
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.OnFocusChangeListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_posting.*

class PostingFragment : Fragment() {

    private lateinit var selectTopicBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var selectPicBehavior: BottomSheetBehavior<MaterialCardView>

    private lateinit var binding: FragmentPostingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.let {
            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }
        select_pic_from_gallery_button.setOnClickListener {
            pickImageFromGallery()
        }
        OnFocusChangeListener(context).let {
            title_edittext?.onFocusChangeListener = it
            message_edittext?.onFocusChangeListener = it
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
            binding.topics?.let {
                if (it.isNotEmpty()) {
                    selectTopicBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.post.topic = it[position]
                    binding.viewmodel = viewModel
                    submit_button.isEnabled = isPostReady
                }
            }
        }
        select_topic_button.setOnClickListener {
            selectTopicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        select_pic_button.setOnClickListener {
            selectPicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        toolbar?.setNavigationOnClickListener {
            viewModel.navigateUp(it)
        }
        submit_button.setOnClickListener {
            binding.viewmodel = viewModel.submitPost(it)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.title.observe(this, Observer {
            viewModel.post.title = it
            submit_button.isEnabled = isPostReady
        })
        viewModel.message.observe(this, Observer {
            viewModel.post.message = it
            submit_button.isEnabled = isPostReady
        })
        viewModel.topics.observe(this, Observer {
            binding.topics = it
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE -> {
                        viewModel.let {
                            it.imageUri = data?.data!!
                            binding.viewmodel = it
                        }
                    }
                }
            }
        }
    }

    private val isPostReady: Boolean
        get() {
            viewModel.post.run {
                return title.isNotBlank() && message.isNotBlank() && topic.id.isNotBlank() && viewModel.imageUri != Uri.EMPTY
            }
        }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        }
        startActivityForResult(intent, Constants.PICK_IMAGE_FROM_GALLERY_REQUEST_CODE)
    }

}