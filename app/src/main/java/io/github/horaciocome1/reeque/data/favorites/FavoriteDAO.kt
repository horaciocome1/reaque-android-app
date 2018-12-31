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

package io.github.horaciocome1.reeque.data.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class FavoriteDAO {

    private val topicsList = mutableListOf<Favorite>()
    private val topics = MutableLiveData<List<Favorite>>()

    private val postsList = mutableListOf<Favorite>()
    private val posts = MutableLiveData<List<Favorite>>()

    init {
        posts().forEach { postsList += it }
        posts().forEach { postsList += it }
        posts().forEach { postsList += it }
        posts().forEach { postsList += it }

        topics().forEach { topicsList += it }
        topics().forEach { topicsList += it }
        topics().forEach { topicsList += it }
        topics().forEach { topicsList += it }

        topics.value = topicsList
        posts.value = postsList
    }

    fun getTopics() = topics as LiveData<List<Favorite>>

    fun getPosts() = posts as LiveData<List<Favorite>>

    fun posts() = arrayListOf(
        Favorite("Ontem? Talvez."),
        Favorite("Vida A Dois ++"),
        Favorite("A Dor De Um Semut"),
        Favorite("Nao Dois, Mas Tres"),
        Favorite("Lago Niassa"),
        Favorite("Kharinghana Wa Kharinghana"),
        Favorite("Semblante"),
        Favorite("Afeto Levado a Serio")
    )

    fun topics() = arrayListOf(
        Favorite("Casamento"),
        Favorite("Lugares"),
        Favorite("Justica Economica"),
        Favorite("Starups Africanas"),
        Favorite("Pobreza Extrema"),
        Favorite("Cronicas Africanas"),
        Favorite("Contos")
    )

}