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
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.notifications.Notification
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }


    override fun onStart() {
        super.onStart()
        fragment_notifications_recyclerview.run {
            layoutManager = LinearLayoutManager(context)
            adapter = NotificationsAdapter(notifications)
        }
        fragment_notifications_progressbar.visibility = View.GONE
    }

}

val notifications = mutableListOf(
    Notification("").apply {
        date = "22, Abril de 2018"
        message =
            "Lorem ipsum dolor sit amet, pri nusquam euripidis voluptaria ea, eam et ullum reprehendunt. Usu mucius phaedrum posidonium no. Ex eos equidem explicari, cibo rebum ludus mei no. In usu quaeque civibus, dicant dolores mea ad. Saperet periculis ad sea, et paulo alterum efficiendi vel."
    },
    Notification("").apply {
        date = "22, Julho de 2018"
        message = "Et adolescens ullamcorper sit."
    },
    Notification("").apply {
        date = "22, Setembro de 2018"
        message = "Ea pri quaeque pertinax, veritus habemus his ei."
    },
    Notification("").apply {
        date = "22, Julho de 2018"
        message = "His ad expetendis percipitur, duo ut veritus atomorum sensibus, mei cu vocibus eligendi."
    },
    Notification("").apply {
        date = "22, Dezembro de 2018"
        message =
            "Vim ludus graece deterruisset eu, ne iriure accusata theophrastus nec. Modus doming pertinacia ad vel. Ex his possit civibus voluptatum."
    },
    Notification("").apply {
        date = "22, Julho de 2018"
        message =
            "Vim ludus graece deterruisset eu, ne iriure accusata theophrastus nec. Modus doming pertinacia ad vel. Ex his possit civibus voluptatum. Sint illud modus quo et, affert appareat voluptatibus nam ei, ea pri populo reformidans. Id sit nostrum deseruisse. Pri aliquando forensibus complectitur an, probo electram vulputate eu his."
    },
    Notification("").apply {
        date = "22, Fevereiro de 2018"
        message = "Et adolescens ullamcorper sit."
    }
)