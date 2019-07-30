/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
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

package io.github.horaciocome1.reaque.data.users

import io.github.horaciocome1.reaque.util.string

data class User(var id: String) {

    var name = ""
    var bio = ""
    var pic = ""
    var address = ""
    var email = ""
    var since = ""
    var timestamp = com.google.firebase.Timestamp.now()
        set(value) {
            since = value.string
            field = value
        }
    var topTopic = ""
    var posts = "0"
    var subscribers = "0"
    var subscriptions = "0"
    var score = 0f

}