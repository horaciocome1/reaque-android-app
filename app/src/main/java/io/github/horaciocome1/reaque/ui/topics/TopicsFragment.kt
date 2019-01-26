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

package io.github.horaciocome1.reaque.ui.topics

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_topics.*

class TopicsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onStart() {
        super.onStart()
        var list = listOf<Topic>()
        getTopicsViewModel().getTopics().observe(this, Observer { topics ->
            when {
                topics.isEmpty() -> fragment_topics_recyclerview.visibility = View.GONE
                list.isEmpty() -> {
                    list = topics
                    configList(list)
                    fragment_topics_recyclerview.visibility = View.VISIBLE
                    fragment_topics_progressbar.visibility = View.GONE
                }
                topics != list -> {
                    fragment_topics_tap_to_update_button.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            visibility = View.GONE
                            list = topics
                            configList(list)
                        }
                    }
                }
            }
        })
    }

    private fun configList(list: List<Topic>) = fragment_topics_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = TopicsAdapter(list)
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
    }

}