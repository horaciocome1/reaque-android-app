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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.*

class CommentsWebService {

    private val tag = "CommentsWebService"
    private val topicIdField = "topic.id"
    private val postIdField = "post.id"

    private var topicCommentsList = mutableListOf<Comment>()
    private val topicComments = MutableLiveData<List<Comment>>()
    private var topicId = ""

    private var postCommentsList = mutableListOf<Comment>()
    private val postComments = MutableLiveData<List<Comment>>()
    private var postId = ""

    private val ref = FirebaseFirestore.getInstance().collection("comments")

    private lateinit var auth: FirebaseAuth

    fun submitComment(comment: Comment, onSuccessful: () -> Unit) {
        auth = FirebaseAuth.getInstance()
        comment.user.run {
            auth.currentUser?.let {
                id = it.uid
                name = it.displayName!!
                pic = it.photoUrl.toString()
            }
        }
        ref.add(comment.hashMap).addOnCompleteListener {
            if (it.isSuccessful)
                onSuccessful()
            else
                onUploadFailed(tag)
        }
    }

    fun getComments(topic: Topic): LiveData<List<Comment>> {
        if (!topic.id.equals(topicId, true)) {
            topicComments.value = mutableListOf()
            ref.whereEqualTo(topicIdField, topic.id)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            topicCommentsList = mutableListOf()
                            for (doc in snapshot)
                                topicCommentsList.add(doc.comment)
                            topicComments.value = topicCommentsList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
        }
        topicId = topic.id
        return topicComments
    }

    fun getComments(post: Post): LiveData<List<Comment>> {
        if (!post.id.equals(postId, true)) {
            postComments.value = mutableListOf()
            ref.whereEqualTo(postIdField, post.id)
                .addSnapshotListener { snapshot, exception ->
                    when {
                        exception != null -> onListenFailed(tag, exception)
                        snapshot != null -> {
                            postCommentsList = mutableListOf()
                            for (doc in snapshot)
                                postCommentsList.add(doc.comment)
                            postComments.value = postCommentsList
                        }
                        else -> onSnapshotNull(tag)
                    }
                }
        }
        postId = post.id
        return postComments
    }

}