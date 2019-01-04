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

package io.github.horaciocome1.reeque.data.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.topics.Topic
import io.github.horaciocome1.reeque.data.users.User

class PostDAO {

    private val postList = mutableListOf<Post>()
    private val posts = MutableLiveData<List<Post>>()
    private val post = MutableLiveData<Post>()

    init {
        posts().forEach { postList += it }
        posts().forEach { postList += it }
        posts().forEach { postList += it }
        posts().forEach { postList += it }
        posts().forEach { postList += it }
        posts().forEach { postList += it }
        posts().forEach { postList += it }
        post.value = posts()[0]



        posts.value = postList
//        post.value = Post("")
    }

    fun addPost(post: Post) {
        postList += post
        posts.value = postList
    }

    fun getPosts(topic: Topic) = posts as LiveData<List<Post>>

    fun getPosts(topic: Topic, user: User) = posts as LiveData<List<Post>>

    fun getPosts(key: String) = post as LiveData<Post>




    // for testing only
    private fun posts() = arrayListOf(
        Post("A Dor de Um Semut").apply {
            rating = 4.7f
            date = "02.2018"
            topic = "Amor"
            user = User("Matilde Zen Mar").apply { pic = R.drawable.profile2 }
            cover = R.drawable.profile3
            message = "Não acredite em algo simplesmente porque ouviu. Não acredite em algo simplesmente porque todos falam a respeito. Não acredite em algo simplesmente porque está escrito em seus livros religiosos. Não acredite em algo só porque seus professores e mestres dizem que é verdade. Não acredite em tradições só porque foram passadas de geração em geração. Mas depois de muita análise e observação, se você vê que algo concorda com a razão, e que conduz ao bem e beneficio de todos, aceite-o e viva-o."
        },
        Post("Vida a Dois ++").apply {
            rating = 2.9f
            date = "12.2018"
            user = User("Lucas Lui").apply { pic = R.drawable.profile3 }
            cover = R.drawable.profile2
            topic = "Casamento"
            message = "Não acredite em algo simplesmente porque ouviu. Não acredite em algo simplesmente porque todos falam a respeito. Não acredite em algo simplesmente porque está escrito em seus livros religiosos. Não acredite em algo só porque seus professores e mestres dizem que é verdade. Não acredite em tradições só porque foram passadas de geração em geração. Mas depois de muita análise e observação, se você vê que algo concorda com a razão, e que conduz ao bem e beneficio de todos, aceite-o e viva-o."
        },
        Post("Eu E A Cinderella").apply {
            rating = 4.8f
            date = "10.2018"
            user = User("Matilde Zen Mar").apply { pic = R.drawable.profile2 }
            cover = R.drawable.profile3
            topic = "Amor"
            message = "Não acredite em algo simplesmente porque ouviu. Não acredite em algo simplesmente porque todos falam a respeito. Não acredite em algo simplesmente porque está escrito em seus livros religiosos. Não acredite em algo só porque seus professores e mestres dizem que é verdade. Não acredite em tradições só porque foram passadas de geração em geração. Mas depois de muita análise e observação, se você vê que algo concorda com a razão, e que conduz ao bem e beneficio de todos, aceite-o e viva-o."
        },
        Post("Sucesso X Amor").apply {
            rating = 5.0f
            date = "08.2018"
            user = User("Matilde Zen Mar").apply { pic = R.drawable.profile3 }
            cover = R.drawable.profile2
            topic = "Conselhos"
            message = "Não acredite em algo simplesmente porque ouviu. Não acredite em algo simplesmente porque todos falam a respeito. Não acredite em algo simplesmente porque está escrito em seus livros religiosos. Não acredite em algo só porque seus professores e mestres dizem que é verdade. Não acredite em tradições só porque foram passadas de geração em geração. Mas depois de muita análise e observação, se você vê que algo concorda com a razão, e que conduz ao bem e beneficio de todos, aceite-o e viva-o."
        }
    )

}
