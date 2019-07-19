package io.github.horaciocome1.reaque.ui.posts.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.storage.StorageRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository

class CreatePostViewModelFactory(
    private val postsRepository: PostsRepository,
    private val topicsRepository: TopicsRepository,
    private val storageRepository: StorageRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CreatePostViewModel(
        topicsRepository,
        postsRepository,
        storageRepository
    ) as T

}