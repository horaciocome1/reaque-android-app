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

package io.github.horaciocome1.reaque.data.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.onListenFailed
import io.github.horaciocome1.reaque.utilities.onSnapshotNull
import io.github.horaciocome1.reaque.utilities.toComment

//FRWsZTrrI0PTp1Fqftdb
//Augusto Filipe
//http://manda-te.com/blog/wp-content/uploads/2014/07/bored-manda-te-700x360.jpg
//
//IojGZNYQm40Zl7Xe59lx
//Matilde Zen Kardif
//https://osegredo.com.br/wp-content/uploads/2017/09/O-que-as-pessoas-felizes-t%C3%AAm-em-comum-site-830x450.jpg

class CommentsWebService {

    private val tag = "CommentsWebService"

    private var topicCommentsList = mutableListOf<Comment>()
    private val topicComments = MutableLiveData<List<Comment>>()
    private var topicId = ""

    private var postCommentsList = mutableListOf<Comment>()
    private val postComments = MutableLiveData<List<Comment>>()
    private var postId = ""

    private val ref = FirebaseFirestore.getInstance().collection("comments")

    fun getComments(topic: Topic): LiveData<List<Comment>> {
        if (!topic.id.equals(topicId, true)) {
            topicComments.value = mutableListOf()
            topicId = topic.id
            ref.whereEqualTo(topicId, true).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        topicCommentsList = mutableListOf()
                        for (doc in snapshot)
                            topicCommentsList.add(
                                doc.toComment()
                            )
                        topicComments.value = topicCommentsList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return topicComments
    }

    fun getComments(post: Post): LiveData<List<Comment>> {
        if (!post.id.equals(postId, true)) {
            postComments.value = mutableListOf()
            postId = post.id
            ref.whereEqualTo(postId, true).addSnapshotListener { snapshot, exception ->
                when {
                    exception != null -> onListenFailed(tag, exception)
                    snapshot != null -> {
                        postCommentsList = mutableListOf()
                        for (doc in snapshot)
                            postCommentsList.add(
                                doc.toComment()
                            )
                        postComments.value = postCommentsList
                    }
                    else -> onSnapshotNull(tag)
                }
            }
        }
        return postComments
    }

    private fun generate() = mutableListOf(
        Comment("").apply {
            message =
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
            writerName = "Gil Andre Loque"
            writerPic = "http://www.sbie.com.br/wp-content/uploads/2017/05/1-pessoa-ruim.jpg"
            date = "15, Abril 2018"
            totalLikes = 334
        },
        Comment("").apply {
            message =
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
            writerName = "Fernanda Miguel"
            writerPic = "https://cdn-images-1.medium.com/max/2600/1*NgAsJaopiSn3RnsFvBr1IQ.jpeg"
            date = "28, Abril de 2018"
            totalLikes = 78
        },
        Comment("").apply {
            message =
                    "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur?"
            writerName = "Amelia Lourenco Augusto"
            writerPic = "https://s3.eplaces.com.br/_default/yogacomvoce/img-posts/c06935be5ec0377fbfe2675a16813429.jpg"
            date = "30, Abril de 2018"
            totalLikes = 9
        },
        Comment("").apply {
            message =
                    "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio."
            writerName = "Angelo M."
            writerPic = "http://www.ingredientesecreto.tv/media/uploads/HenriqueSP_is-still_serie4semletras.jpg"
            date = "04, Agosto de 2018"
            totalLikes = 1053
        }
    )

}