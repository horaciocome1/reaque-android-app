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

package io.github.horaciocome1.reeque.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.ui.fragmentManager
import io.github.horaciocome1.reeque.utilities.InjectorUtils

fun FragmentManager.getTopicsListFragment(): Fragment {
    fragmentManager = this
    return TopicsListFragment()
}


class TopicsListFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view is RecyclerView) {
            val factory = InjectorUtils.provideFavoritesViewModelFactory()
            val viewModel = ViewModelProviders.of(this, factory)[FavoritesViewModel::class.java]
            viewModel.getTopics().observe(this, Observer {
                view.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = FavoritesAdapter(context, it)
                }
            })
        }
    }

}