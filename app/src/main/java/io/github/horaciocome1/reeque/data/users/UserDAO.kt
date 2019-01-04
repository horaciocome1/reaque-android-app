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

package io.github.horaciocome1.reeque.data.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.horaciocome1.reeque.R
import io.github.horaciocome1.reeque.data.topics.Topic

class UserDAO {

    private var userList = mutableListOf<User>()
    private var users = MutableLiveData<List<User>>()

    private var user = MutableLiveData<User>()

    init {
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        users().forEach { userList.add(it) }
        user.value = users()[0]


        users.value = userList
//        user.value = User("")
    }

    fun addUser(user: User) {
        userList.add(user)
        users.value = userList
    }

    fun getUsers(topic: Topic) = users as LiveData<List<User>>

    fun getUsers(key: String) = user as LiveData<User>

    private fun users() = arrayListOf(
        User("Antonia Marta de Sema Ricardo").apply {
            pic = R.drawable.profile2
            topics = "Amor, Cidadania, Cultura, Casamento, Lugares"
            town = "Ilha de Mocambique"

            description = "17 year old student passionate for cars and urban culture. Thats what i write about.\\nHope you like it.\\n\\nhugs!"
            totalFollowers = "826"
            totalPosts = "57"
            town = "Mocimboa da Praia"
            since = "Setembro de 2018"
            latestPost = "Não acredite em algo simplesmente porque ouviu. Não acredite em algo simplesmente porque todos falam a respeito. Não acredite em algo simplesmente porque está escrito em seus livros religiosos. Não acredite em algo só porque seus professores e mestres dizem que é verdade. Não acredite em tradições só porque foram passadas de geração em geração. Mas depois de muita análise e observação, se você vê que algo concorda com a razão, e que conduz ao bem e beneficio de todos, aceite-o e viva-o."
            email = "aricardo968@zmail.com"
        },
        User("Ana Ju").apply {
            pic = R.drawable.profile3
            topics = "Cultura, Casamento"
            town = "Maputo"
        },
        User("Cedro de Carlos").apply {
            pic = R.drawable.profile2
            topics = "Amor"
            town = "Cidade do Cabo"
        },
        User("Julia Ju").apply {
            pic = R.drawable.profile3
            topics = "Cidadania, Cultura, Casamento, Lugares"
            town = "Chimoio"
        }
    )

}