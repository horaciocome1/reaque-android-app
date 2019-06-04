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

package io.github.horaciocome1.reaque.data.topics

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.util.addSimpleAndSafeSnapshotListener
import io.github.horaciocome1.reaque.util.topicsForPosts
import io.github.horaciocome1.reaque.util.topicsForUsers

class TopicsWebService {

    private val tag = "TopicsWebService"
    private val empty = 0

    private val ref = FirebaseFirestore.getInstance().collection("topics")

    private val auth = FirebaseAuth.getInstance()

    private var notEmptyTopicsForPostsList = mutableListOf<Topic>()
    val notEmptyTopicsForPosts = MutableLiveData<List<Topic>>()
        get() {
            if (notEmptyTopicsForPostsList.isEmpty())
                ref.whereGreaterThan("posts_count", empty).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                    notEmptyTopicsForPostsList = snapshot.topicsForPosts
                    field.value = snapshot.topicsForPosts
                }
            return field
        }

    private var notEmptyTopicsForUsersList = mutableListOf<Topic>()
    val notEmptyTopicsForUsers = MutableLiveData<List<Topic>>()
        get() {
            if (notEmptyTopicsForUsersList.isEmpty())
                ref.whereGreaterThan("users_count", empty).addSimpleAndSafeSnapshotListener(tag, auth) { snapshot, _ ->
                    notEmptyTopicsForUsersList = snapshot.topicsForUsers
                    field.value = snapshot.topicsForUsers
                }
            return field
        }

    private var topicsList = mutableListOf<Topic>()
    val topics = MutableLiveData<List<Topic>>()
        get() {
            if (topicsList.isEmpty())
                ref.orderBy("title", Query.Direction.ASCENDING).addSimpleAndSafeSnapshotListener(
                    tag,
                    auth
                ) { snapshot, _ ->
                    topicsList = snapshot.topicsForPosts
                    field.value = snapshot.topicsForPosts
                }
            return field
        }

}