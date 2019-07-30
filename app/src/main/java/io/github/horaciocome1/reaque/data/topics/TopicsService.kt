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
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.github.horaciocome1.reaque.util.safeGet
import io.github.horaciocome1.reaque.util.topics

class TopicsService {

    private val ref: CollectionReference by lazy { FirebaseFirestore.getInstance().collection("topics") }

    private var _notEmptyTopics = mutableListOf<Topic>()

    val notEmptyTopics = MutableLiveData<List<Topic>>()
        get() {
            if (_notEmptyTopics.isEmpty())
                ref.whereGreaterThan("score", 0).orderBy("score", Query.Direction.DESCENDING)
                    .safeGet {
                        _notEmptyTopics = it.topics
                        field.value = _notEmptyTopics
                    }
            return field
        }

    private var _topics = mutableListOf<Topic>()

    val topics = MutableLiveData<List<Topic>>()
        get() {
            if (_topics.isEmpty())
                ref.orderBy("title", Query.Direction.ASCENDING)
                    .safeGet {
                        _topics = it.topics
                        field.value = _topics
                    }
            return field
        }

}