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

package io.github.horaciocome1.reaque.ui.comments

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.comments.Comment
import io.github.horaciocome1.reaque.data.comments.CommentsRepository
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.utilities.InjectorUtils

val CommentsFragment.viewModel: CommentsViewModel
    get() {
        val factory = InjectorUtils.commentsViewModelFactory
        return ViewModelProviders.of(this, factory)[CommentsViewModel::class.java]
    }

class CommentsViewModel(private val repository: CommentsRepository) : ViewModel() {

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

    fun getComments(topic: Topic) = repository.getComments(topic)

    fun getComments(post: Post) = repository.getComments(post)

    fun submitComment() {
        _isSubmitting.value = true
        repository.submitComment(comment) {
            _isFinished.value = true
            _isSubmitting.value = false
        }
    }

}