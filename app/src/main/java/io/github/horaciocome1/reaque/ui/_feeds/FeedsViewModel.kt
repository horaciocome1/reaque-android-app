package io.github.horaciocome1.reaque.ui._feeds

import androidx.lifecycle.ViewModel
import io.github.horaciocome1.reaque.data.feeds.FeedsService

class FeedsViewModel(private val service: FeedsService) : ViewModel() {

    fun get() = service.get()

}