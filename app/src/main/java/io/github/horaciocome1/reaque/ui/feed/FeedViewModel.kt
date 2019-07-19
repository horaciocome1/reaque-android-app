package io.github.horaciocome1.reaque.ui.feed

import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.feed.FeedRepository

class FeedViewModel(private val repository: FeedRepository) : ViewModel() {

    fun get() = repository.get()

}