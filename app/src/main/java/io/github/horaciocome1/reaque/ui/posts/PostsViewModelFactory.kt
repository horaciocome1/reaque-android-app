package io.github.horaciocome1.reaque.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.posts.PostsRepository

class PostsViewModelFactory(
    private val postsRepository: PostsRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        PostsViewModel(postsRepository, bookmarksRepository) as T
}