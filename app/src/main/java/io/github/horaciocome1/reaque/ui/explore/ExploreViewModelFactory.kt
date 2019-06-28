package io.github.horaciocome1.reaque.ui.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository

class ExploreViewModelFactory(
    private val topicsRepository: TopicsRepository,
    private val postsRepository: PostsRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        ExploreViewModel(topicsRepository, postsRepository) as T

}