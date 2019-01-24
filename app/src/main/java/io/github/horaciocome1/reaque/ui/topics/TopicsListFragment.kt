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

package io.github.horaciocome1.reaque.ui.topics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.favorites.getFavoritesViewModel
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import io.github.horaciocome1.reaque.ui.topics.details.loadTopic
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_topics_list.*

fun FragmentManager.getTopicsListFragment(): TopicsListFragment {
    fragmentManager = this
    return TopicsListFragment()
}


class TopicsListFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_topics_list, container, false)
    }

    override fun onStart() {
        super.onStart()
        getFavoritesViewModel().getTopics().observe(this, Observer { topics ->
            var list = listOf<Topic>()
            when {
                topics.isEmpty() -> fragment_topics_list_recyclerview.visibility = View.GONE
                list.isEmpty() -> {
                    list = topics
                    configList(list)
                    fragment_topics_list_recyclerview.visibility = View.VISIBLE
                    fragment_topics_list_progressbar.visibility = View.GONE
                }
                topics != list -> {
                    fragment_topics_list_tap_to_update_button.run {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            list = topics
                            configList(list)
                            visibility = View.GONE
                        }
                    }
                }
            }
        })
    }

    private fun configList(list: List<Topic>) = fragment_topics_list_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = FavoriteTopicAdapter(context, list)
        setOnClick { _, position -> fragmentManager?.loadTopic(list[position]) }
        addSimpleTouchListener()
    }

}