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

package io.github.horaciocome1.reaque.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addSimpleTouchListener
import io.github.horaciocome1.simplerecyclerviewtouchlistener.setOnClick
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.notifications.observe(this, Observer {
            if (it.isEmpty())
                fragment_notifications_progressbar.visibility = View.VISIBLE
            else {
                fragment_notifications_recyclerview.run {
                    layoutManager = LinearLayoutManager(context)
                    adapter = NotificationsAdapter(it)
                    setOnClick { view, position ->
                        it[position].run {
                            when {
                                isComment -> {
                                    val openComments =
                                        NotificationsFragmentDirections.actionOpenCommentsFromNotifications(
                                            id2,
                                            "",
                                            true,
                                            false
                                        )
                                    Navigation.findNavController(view).navigate(openComments)
                                }
                                isPost -> {
                                    val openRead = NotificationsFragmentDirections.actionOpenReadFromNotifications(id2)
                                    Navigation.findNavController(view).navigate(openRead)
                                }
                                isUser -> {
                                    val openProfile =
                                        NotificationsFragmentDirections.actionOpenProfileFromNotifications(id2)
                                    Navigation.findNavController(view).navigate(openProfile)
                                }
                            }
                        }
                    }
                    addSimpleTouchListener()
                }
                fragment_notifications_progressbar.visibility = View.GONE
            }
        })
    }

}