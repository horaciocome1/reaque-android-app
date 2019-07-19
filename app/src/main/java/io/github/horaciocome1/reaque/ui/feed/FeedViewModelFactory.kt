package io.github.horaciocome1.reaque.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.feed.FeedRepository

class FeedViewModelFactory(private val repository: FeedRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(repository) as T

}