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

package io.github.horaciocome1.reaque.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.databinding.FragmentForumBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.posts.SimpleTopicsAdapter
import io.github.horaciocome1.reaque.ui.posts.TopicsAdapter
import io.github.horaciocome1.reaque.utilities.isPortrait
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_forum.*

class ForumFragment : Fragment() {

    private var topics = listOf<Topic>()

    private lateinit var behavior: BottomSheetBehavior<LinearLayout>

    lateinit var binding: FragmentForumBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(select_topics_bottomsheet).apply {
            skipCollapsed = true
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        topics_recyclerview.run {
            setOnClick { _, position ->
                if (topics.isNotEmpty()) {
                    viewModel.comment.topic = topics[position]
                    binding.viewmodel = viewModel
                    viewModel.getComments(viewModel.comment.topic).observe(this@ForumFragment, Observer { comments ->
                        comments_recyclerview.run {
                            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, true)
                            adapter = CommentsAdapter(comments)
                        }
                        comments_progressbar.visibility = if (comments.isEmpty()) View.VISIBLE else View.GONE
                    })
                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
            addSimpleTouchListener()
        }
        select_topic_button.setOnClickListener {
            if (isPortrait)
                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    override fun onStart() {
        super.onStart()
        binding.viewmodel = viewModel
        behavior.run {
            if (state == BottomSheetBehavior.STATE_HIDDEN)
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        viewModel.topics.observe(this, Observer {
            topics = it
            topics_recyclerview.run {
                layoutManager = LinearLayoutManager(context)
                adapter = if (isPortrait) SimpleTopicsAdapter(topics) else TopicsAdapter(topics)
            }
            topics_progressbar.visibility = if (topics.isEmpty()) View.VISIBLE else View.GONE
        })

    }

    override fun onResume() {
        super.onResume()
        if (isPortrait)
            (activity as MainActivity).supportActionBar?.hide()
    }

}