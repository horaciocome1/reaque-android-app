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
import com.google.android.material.bottomappbar.BottomAppBar
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.fragmentManager
import kotlinx.android.synthetic.main.fragment_topics.*

fun FragmentManager.loadTopics() {
    beginTransaction().replace(R.id.activity_main_container, TopicsFragment()).commit()
    fragmentManager = this
}

class TopicsFragment : Fragment() {

    private lateinit var bottomAppBarShadow: View
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomAppBar = (activity as MainActivity).findViewById(R.id.activity_main_bottomappbar)
        bottomAppBarShadow = (activity as MainActivity).findViewById(R.id.activity_main_bottomappbar_shadow)
        return inflater.inflate(R.layout.fragment_topics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomAppBar.visibility = View.VISIBLE
        bottomAppBarShadow.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        var list = listOf<Topic>()
        getTopicsViewModel().getTopics().observe(this, Observer { topics ->
            when {
                topics.isEmpty() -> {
                    fragment_topics_recyclerview.visibility = View.GONE
                    fragment_topics_appbarlayout.visibility = View.GONE
                    bottomAppBar.visibility = View.GONE
                    bottomAppBarShadow.visibility = View.GONE
                }
                list.isEmpty() -> {
                    list = topics
                    configList(list)
                    bottomAppBar.visibility = View.VISIBLE
                    bottomAppBarShadow.visibility = View.VISIBLE
                    fragment_topics_appbarlayout.visibility = View.VISIBLE
                    fragment_topics_recyclerview.visibility = View.VISIBLE
                    fragment_topics_progressbar.visibility = View.GONE
                }
                topics != list -> {

                }
            }
        })
    }

    private fun configList(list: List<Topic>) = fragment_topics_recyclerview.apply {
        layoutManager = LinearLayoutManager(context)
        adapter = TopicsAdapter(context, list, fragmentManager)
    }

    override fun onStop() {
        super.onStop()
        bottomAppBar.visibility = View.GONE
        bottomAppBarShadow.visibility = View.GONE
    }

}