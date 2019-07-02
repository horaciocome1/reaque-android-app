package io.github.horaciocome1.reaque.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.ratings.RatingsRepository
import io.github.horaciocome1.reaque.data.readings.ReadingsRepository
import io.github.horaciocome1.reaque.data.shares.SharesRepository

class PostsViewModelFactory(
    private val postsRepository: PostsRepository,
    private val readingsRepository: ReadingsRepository,
    private val sharesRepository: SharesRepository,
    private val ratingsRepository: RatingsRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = PostsViewModel(
        postsRepository, readingsRepository, sharesRepository, ratingsRepository, bookmarksRepository
    ) as T
}