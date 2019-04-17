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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.utilities.isPortrait
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    private var notifications = listOf<Notification>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview.run {
            addOnItemClickListener { view, position ->
                if (notifications.isNotEmpty()) {
                    notifications[position].run {
                        when {
                            isPost -> view.read(contentId)
                            isUser -> view.openProfile(contentId)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.notifications.observe(this, Observer {
            notifications = it
            recyclerview.run {
                layoutManager = LinearLayoutManager(context)
                adapter = NotificationsAdapter(notifications)
            }
            if (notifications.isEmpty())
                progressbar.visibility = View.VISIBLE
            else
                progressbar.visibility = View.GONE
        })
    }

    override fun onResume() {
        super.onResume()
        if (isPortrait)
            activity.run {
                if (this is MainActivity)
                    supportActionBar?.hide()
            }
    }

    private fun View.openProfile(userId: String) {
        val directions = NotificationsFragmentDirections.actionOpenProfileFromNotifications(userId)
        findNavController().navigate(directions)
    }

    private fun View.read(postId: String) {
        val directions = NotificationsFragmentDirections.actionOpenReadFromNotifications(postId)
        findNavController().navigate(directions)
    }

}