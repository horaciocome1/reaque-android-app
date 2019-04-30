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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.util.notifications
import io.github.horaciocome1.reaque.util.onListenFailed
import io.github.horaciocome1.reaque.util.onSnapshotNull

class NotificationsWebService {

    private val tag = "NotificationsWebService"

    private var notificationsList = mutableListOf<Notification>()

    private val ref = FirebaseFirestore.getInstance().collection("notifications")

    private lateinit var auth: FirebaseAuth

    val notifications = MutableLiveData<List<Notification>>()
        get() {
            if (notificationsList.isEmpty()) {
                auth = FirebaseAuth.getInstance()
                auth.currentUser?.let { user ->
                    ref.whereEqualTo("users.${user.uid}", true)
                        .addSnapshotListener { snapshot, exception ->
                            when {
                                exception != null -> onListenFailed(tag, exception)
                                snapshot != null -> {
                                    notificationsList = snapshot.notifications
                                    field.value = notificationsList
                                }
                                else -> onSnapshotNull(tag)
                            }
                        }
                }
            }
            return field
        }

}