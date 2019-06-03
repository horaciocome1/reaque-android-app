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

package io.github.horaciocome1.reaque.util

import android.util.Log

fun onListeningFailed(tag: String, exception: Exception) = Log.w(tag, "Listen failed.", exception)

fun onUploadingFailed(tag: String) = Log.w(tag, "Upload failed.")

fun onSnapshotNull(tag: String) = Log.w(tag, "Snapshot is null.")

fun onAddUserSucceed(tag: String) = Log.d(tag, "User successfully written!")

fun onAddUserFailed(tag: String, exception: Exception?) = Log.w(tag, "Error writing document", exception)