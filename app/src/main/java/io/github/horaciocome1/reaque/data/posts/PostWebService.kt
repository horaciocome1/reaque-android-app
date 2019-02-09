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

package io.github.horaciocome1.reaque.data.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull
import io.github.horaciocome1.reaque.utilities.post

class PostWebService {

    private val tag = "PostWebService"

    /*list of all posts from the same topic*/
    private var topicPostList = mutableListOf<Post>()
    private val topicPosts = MutableLiveData<List<Post>>()

    /*list of all posts from the same user*/
    private var userPostList = mutableListOf<Post>()
    private val userPosts = MutableLiveData<List<Post>>()

    /*
    * not a list at all
    * only has one element that is the post the user is reading at the moment
    * i used a list to be easy to check if the post exists or not later
    * */
    private var readingPostList = mutableListOf(Post(""))
    private val readingPosts = MutableLiveData<List<Post>>()

    private val reference = FirebaseFirestore.getInstance().collection("posts")

    private val topic = Topic("")
    private val user = User("")

    fun addPost(post: Post) {
        topicPostList.add(post)
        topicPosts.value = topicPostList
    }

    /*retrieve from remote server all topicPosts from the same topic*/
    fun getPosts(topic: Topic): LiveData<List<Post>> {
        if (!this.topic.id.equals(topic.id, true)) {
            topicPosts.value = mutableListOf()
            reference.whereEqualTo(topic.id, true).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        topicPostList = mutableListOf()
                        for (doc in snapshot.documents)
                            topicPostList.add(doc.post())
                        topicPosts.value = topicPostList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
            this.topic.id = topic.id
        }
        return topicPosts
    }

    /*retrieve from remote server every topicPosts wrote by the specified user*/
    fun getPosts(user: User): LiveData<List<Post>> {
        if (!this.user.id.equals(user.id, true)) {
            userPosts.value = mutableListOf()
            reference.whereEqualTo("writerId", user.id).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        userPostList = mutableListOf()
                        for (doc in snapshot.documents)
                            userPostList.add(doc.post())
                        userPosts.value = userPostList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
            this.user.id = user.id
        }
        return userPosts
    }

    /*retrieve from remote server the post that the user wants to read*/
    fun getPosts(post: Post): LiveData<List<Post>> {
        if (!post.id.equals(readingPostList[0].id, true)) {
            readingPosts.value = mutableListOf()
            reference.document(post.id).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        readingPostList = mutableListOf(snapshot.post())
                        readingPosts.value = readingPostList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return readingPosts
    }

}
