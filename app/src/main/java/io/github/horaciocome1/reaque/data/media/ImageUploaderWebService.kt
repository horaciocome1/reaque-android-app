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
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import io.github.horaciocome1.reaque.util.onUploadFailed

class ImageUploaderWebService {

    private val tag = "ImageUploaderWebService"

    private val storage = FirebaseStorage.getInstance()

    fun upload(uploader: ImageUploader) {
        uploader.run {
            val ref = storage.reference.child("images/${post.topic.id}/${post.id}/${imageUri.lastPathSegment}")
            val uploadTask = ref.putFile(imageUri)
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful)
                    task.exception?.let {
                        throw it
                    }
                return@Continuation ref.downloadUrl
            }).addOnSuccessListener {
                onSuccessListener(it.toString())
            }.addOnFailureListener {
                onUploadFailed(tag)
                onFailureListener()
            }
        }
    }

}