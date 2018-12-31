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

package io.github.horaciocome1.reeque.data.topic

data class Topic(var title: String) {

    var key = ""
    var totalPosts = 0
    var post1Rating = 0f
    var post1Title = ""
    var post1ProfilePic = 0
    var post2Rating = 0f
    var post2Title = ""
    var post2ProfilePic = 0
    var post3Rating = 0f
    var post3Title = ""
    var post3ProfilePic = 0
    var totalReaders = 0

}