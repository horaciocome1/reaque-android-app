package io.github.horaciocome1.reaque.ui.posts

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.android.material.button.MaterialButton
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.ratings.RatingsRepository
import io.github.horaciocome1.reaque.data.readings.ReadingsRepository
import io.github.horaciocome1.reaque.data.shares.SharesRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.buildShortDynamicLink

class PostsViewModel(
    private val postsRepository: PostsRepository,
    private val readingsRepository: ReadingsRepository,
    private val sharesRepository: SharesRepository,
    private val ratingsRepository: RatingsRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {

    private val dynamicLinks: FirebaseDynamicLinks by lazy {
        FirebaseDynamicLinks.getInstance()
    }

    var readingPost = Post("")
        set(value) {
            if (value.id.isNotBlank() && value.id != field.id)
                readingsRepository.read(value) { }
            field = value
        }

    var posts: List<Post> = mutableListOf()

    val onItemClickListener: (View, Int) -> Unit = { view, position ->
        if (posts.isNotEmpty()) {
            val directions = PostsFragmentDirections.actionOpenReadPostFromPosts(posts[position].id)
            view.findNavController().navigate(directions)
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


    fun get(post: Post) = postsRepository.get(post)

    fun getRating(post: Post) = ratingsRepository.get(post)

    fun setRating(view: View, post: Post, value: Int) {
        ratingsRepository.set(post, value) { }
        navigateUp(view)
    }

    fun share(fragment: ReadPostFragment, view: View, post: Post) {
        view.isEnabled = false
        dynamicLinks.buildShortDynamicLink(post) {
            if (fragment.isVisible) {
                fragment.startActivity(it)
                sharesRepository.share(post) { view.isEnabled = true }
            }
        }
    }

    fun bookmark(view: View, post: Post) {
        view.isEnabled = false
        bookmarksRepository.bookmark(post) { view.visibility = View.GONE }
    }

    fun unBookmark(view: View, post: Post) {
        view.isEnabled = false
        bookmarksRepository.unBookmark(post) { view.visibility = View.GONE }
    }

    fun isBookmarked(post: Post) = bookmarksRepository.isBookmarked(post)

    fun openUserProfile(view: View, user: User) {
        val directions = ReadPostFragmentDirections.actionOpenUserProfileFromReadPost(user.id)
        view.findNavController().navigate(directions)
    }

    fun openSetRating(view: View, post: Post) {
        if (view is MaterialButton) {
            val rating = view.text.toString().toInt()
            val directions = ReadPostFragmentDirections.actionOpenSetRating(post.id, rating)
            view.findNavController().navigate(directions)
        }
    }

    fun navigateUp(view: View) = view.findNavController().navigateUp()

}