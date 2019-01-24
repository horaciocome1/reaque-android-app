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

package io.github.horaciocome1.reaque.ui.topics.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import kotlinx.android.synthetic.main.fragment_topic.*

lateinit var topic: Topic

fun FragmentManager.loadTopic(topic: Topic) {
    io.github.horaciocome1.reaque.ui.topics.details.topic = topic
    fragmentManager = this
    val fragment = TopicFragment()
    beginTransaction().replace(R.id.activity_main_container, fragment)
        .addToBackStack(fragment.tag).commit()
}

class TopicFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_topic_back_button.setOnClickListener { activity?.onBackPressed() }
        fragment_topic_more_button.setOnClickListener { }
    }

    override fun onStart() {
        super.onStart()
        fragment_topic_title.text = topic.title
        fragment_topic_viewpager.run {
            adapter = TabAdapter(fragmentManager, topic)
            currentItem = 1
            fragment_topic_tablayout.setupWithViewPager(this, true)
        }
    }

}