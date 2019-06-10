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

package io.github.horaciocome1.reaque.data._storage

import android.net.Uri
import io.github.horaciocome1.reaque.data._topics.Topic

class StorageRepository private constructor(private val webservice: StorageService) {

    fun uploadImage(imageUri: Uri, topic: Topic, onSuccessListener: (String) -> Unit) =
        webservice.upload(imageUri, topic, onSuccessListener)

    companion object {

        @Volatile
        private var instance: StorageRepository? = null

        fun getInstance(webservice: StorageService) = instance ?: synchronized(this) {
            instance ?: StorageRepository(webservice).also {
                instance = it
            }
        }

    }

}