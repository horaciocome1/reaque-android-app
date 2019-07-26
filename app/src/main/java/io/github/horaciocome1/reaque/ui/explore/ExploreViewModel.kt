package io.github.horaciocome1.reaque.ui.explore

import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository

class ExploreViewModel(topicsRepository: TopicsRepository, postsRepository: PostsRepository) : ViewModel() {

    val notEmptyTopics = topicsRepository.notEmptyTopics

    val top10 = postsRepository.getTop10()

}