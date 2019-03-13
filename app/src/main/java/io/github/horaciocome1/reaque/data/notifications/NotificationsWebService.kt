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

package io.github.horaciocome1.reaque.data.notifications

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.utilities.notification
import io.github.horaciocome1.reaque.utilities.onListenFailed

class NotificationsWebService {

    private val tag = "NotificationsWebService"
    private val myId = "FRWsZTrrI0PTp1Fqftdb"

    private val ref = FirebaseFirestore.getInstance().collection("notifications")

    val notifications = MutableLiveData<List<Notification>>()

    init {
        ref.whereEqualTo(myId, true)
            .addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        val notificationsList = mutableListOf<Notification>()
                        for (doc in snapshot)
                            notificationsList.add(doc.notification)
                        notifications.value = notificationsList
                    }
                }
            }
    }

}