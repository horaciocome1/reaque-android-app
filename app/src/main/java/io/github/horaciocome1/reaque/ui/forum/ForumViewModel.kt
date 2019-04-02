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

package io.github.horaciocome1.reaque.ui.forum

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.comments.Comment
import io.github.horaciocome1.reaque.data.comments.CommentsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.utilities.InjectorUtils

val ForumFragment.viewModel: CommentsViewModel
    get() {
        val factory = InjectorUtils.forumViewModelFactory
        return ViewModelProviders.of(this, factory)[CommentsViewModel::class.java]
    }

class CommentsViewModel(
    private val commentsRepository: CommentsRepository,
    private val topicsRepository: TopicsRepository
) : ViewModel() {

    val comment = Comment("")

    @Bindable
    val message = MutableLiveData<String>()

    private val _isFinished = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isFinished: LiveData<Boolean>
        get() = _isFinished

    private val _isSubmitting = MutableLiveData<Boolean>().apply {
        value = false
    }
    val isSubmitting: LiveData<Boolean>
        get() = _isSubmitting

    val topics = topicsRepository.topics

    fun getComments(topic: Topic) = commentsRepository.getComments(topic)

    fun submitComment() {
        _isSubmitting.value = true
        commentsRepository.submitComment(comment) {
            _isFinished.value = true
            _isSubmitting.value = false
        }
    }

}