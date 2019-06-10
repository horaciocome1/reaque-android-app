package io.github.horaciocome1.reaque.ui._topics

import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.topics.TopicsRepository

class TopicsViewmodel(private val topicsRepository: TopicsRepository) : ViewModel() {

    val topicsForUsers = topicsRepository.notEmptyTopicsForUsers

    val topicsForPosts = topicsRepository.notEmptyTopicsForPosts

}