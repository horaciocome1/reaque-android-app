package io.github.horaciocome1.reaque.ui.feed

import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.feeds.FeedsRepository

class FeedViewModel(private val repository: FeedsRepository) : ViewModel() {

    fun get() = repository.get()

}