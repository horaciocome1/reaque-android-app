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

package io.github.horaciocome1.reaque.ui.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.comments.Comment
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_comments.*

class CommentsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_comments, container, false)
    }

    override fun onStart() {
        super.onStart()
        arguments?.let {
            val safeArgs = CommentsFragmentArgs.fromBundle(it)
            val topic = Topic(safeArgs.topicId) // need to go
            (activity as MainActivity).supportActionBar?.title = safeArgs.topicTitle
            var list = listOf<Comment>()
            getCommentsViewModel().getComments(topic).observe(this, Observer { comments ->
                when {
                    comments.isEmpty() -> {
                        fragment_comments_recyclerview.visibility = View.GONE
                        fragment_comments_edittext.visibility = View.GONE
                        fragment_comments_send_button.visibility = View.GONE
                    }
                    list.isEmpty() -> {
                        list = comments
                        configList(list)
                        fragment_comments_recyclerview.visibility = View.VISIBLE
                        fragment_comments_edittext.visibility = View.VISIBLE
                        fragment_comments_send_button.visibility = View.VISIBLE
                        fragment_comments_progressbar.visibility = View.GONE
                    }
                    comments != list -> {
                        fragment_comments_tap_to_update_button.run {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                visibility = View.GONE
                                list = comments
                                configList(list)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun configList(list: List<Comment>) = fragment_comments_recyclerview.apply {
        layoutManager = LinearLayoutManager(context).apply { reverseLayout = true }
        adapter = CommentsAdapter(list)
    }

}