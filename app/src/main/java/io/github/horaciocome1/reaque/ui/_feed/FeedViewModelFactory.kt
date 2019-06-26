package io.github.horaciocome1.reaque.ui._feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.feeds.FeedsRepository

class FeedViewModelFactory(private val repository: FeedsRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FeedViewModel(repository) as T

}