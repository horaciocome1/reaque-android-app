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
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.horaciocome1.reaque.databinding.FragmentPostingBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_posting.*

class PostingFragment : Fragment() {

    lateinit var selectTopicBehavior: BottomSheetBehavior<LinearLayout>

    lateinit var binding: FragmentPostingBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPostingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancel_button.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }
        post_button.isEnabled = false
        selectTopicBehavior = BottomSheetBehavior.from(select_topics_bottomsheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            lifecycleOwner = this@PostingFragment
            this.viewmodel = viewModel
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
            topics_recyclerview.run {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = TopicsAdapter(it)
                setOnClick { _, position ->
                    selectTopicBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.post.topic = it[position]
                    binding.viewmodel = viewModel
                    post_button.isEnabled = isPostReady
                }
                addSimpleTouchListener()
            }
        })
        select_topic_button.setOnClickListener {
            selectTopicBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private val isPostReady: Boolean
        get() {
            viewModel.post.run {
                return (title.isNotBlank() && message.isNotBlank() && topic.id.isNotBlank())
            }
        }

}