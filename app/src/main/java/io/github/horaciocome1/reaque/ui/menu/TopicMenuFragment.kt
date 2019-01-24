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

package io.github.horaciocome1.reaque.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.fragmentManager
import kotlinx.android.synthetic.main.fragment_topic_menu.*

lateinit var topic: Topic

fun FragmentManager.loadTopicMenu(topic: Topic) {
    val fragment = TopicMenuFragment()
    fragment.show(this, fragment.tag)
    io.github.horaciocome1.reaque.ui.menu.topic = topic
    fragmentManager = this
}

class TopicMenuFragment : BottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topic_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_topic_menu_posts_textview.setOnClickListener {
            //            fragmentManager?.loadPosts(topic)
            dismiss()
        }
        fragment_topic_menu_writers_textview.setOnClickListener {
            //            fragmentManager?.loadUsers(topic)
            dismiss()
        }
        fragment_topic_menu_discussion_textview.setOnClickListener {
            //            fragmentManager?.loadDiscussion(topic)
            dismiss()
        }
    }

}