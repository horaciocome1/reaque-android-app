package io.github.horaciocome1.reaque.ui.posts

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.Constants

class PostsViewModel(
    private val postsRepository: PostsRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {

    var posts: List<Post> = mutableListOf()

    val onItemClickListener: (View, Int) -> Unit = { view, position ->
        if (posts.isNotEmpty()) {
            val directions = PostsFragmentDirections
                .actionOpenReadPostFromPosts(posts[position].id)
            view.findNavController()
                .navigate(directions)
        }
    }

    fun setPosts(posts: List<Post>): PostsViewModel {
        this.posts = posts
        return this
    }


    fun get(parentId: String, requestId: String): LiveData<List<Post>> {
        return when (requestId) {
            Constants.TOPIC_POSTS_REQUEST -> postsRepository.get((Topic(parentId)))
            Constants.USER_POSTS_REQUEST -> postsRepository.get((User(parentId)))
            else -> bookmarksRepository.get()
        }
    }

}