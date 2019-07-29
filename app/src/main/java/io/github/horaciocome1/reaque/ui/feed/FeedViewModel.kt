package io.github.horaciocome1.reaque.ui.feed

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.feed.FeedRepository
import io.github.horaciocome1.reaque.data.posts.Post

class FeedViewModel(private val repository: FeedRepository) : ViewModel() {

    var posts: List<Post> = mutableListOf()

    val onItemClickListener: (View, Int) -> Unit = { view, position ->
        if (posts.isNotEmpty()) {
            val directions = FeedFragmentDirections.actionOpenReadPostFromFeed(posts[position].id)
            view.findNavController().navigate(directions)
        }
    }

    fun setPosts(posts: List<Post>): FeedViewModel {
        this.posts = posts
        return this
    }

    fun get() = repository.get()

}