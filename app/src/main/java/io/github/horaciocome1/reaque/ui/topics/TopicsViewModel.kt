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

package io.github.horaciocome1.reaque.ui.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.utilities.InjectorUtils

val TopicsFragment.viewModel: TopicsViewModel
    get() {
        val factory = InjectorUtils.topicsViewModelFactory
        return ViewModelProviders.of(this, factory)[TopicsViewModel::class.java]
    }

class TopicsViewModel(private val topicsRepository: TopicsRepository) : ViewModel() {

    fun addTopic(topic: Topic) = topicsRepository.addTopic(topic)

    val topics = topicsRepository.topics

}