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

package io.github.horaciocome1.reaque.data.media

import android.net.Uri
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic

class ImageRepository private constructor(private val imageRepository: ImageRepository) {

    fun uploadImage(
        imageUri: Uri, topic: Topic, post: Post,
        onComplete: (String) -> Unit, onFailure: () -> Unit
    ) = imageRepository.uploadImage(
        imageUri = imageUri,
        topic = topic,
        post = post,
        onComplete = onComplete,
        onFailure = onFailure
    )

}