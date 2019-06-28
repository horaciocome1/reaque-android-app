package io.github.horaciocome1.reaque.ui.posts

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.ratings.RatingsRepository
import io.github.horaciocome1.reaque.data.readings.ReadingsRepository
import io.github.horaciocome1.reaque.data.shares.SharesRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.Constants

class PostsViewModel(
    private val postsRepository: PostsRepository,
    private val readingsRepository: ReadingsRepository,
    private val sharesRepository: SharesRepository,
    private val ratingsRepository: RatingsRepository,
    private val bookmarksRepository: BookmarksRepository
) : ViewModel() {

    fun get(id: String): LiveData<List<Post>> {
        return if (id == Constants.BOOKMARKS_REQUEST)
            bookmarksRepository.get()
        else {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null)
                if (id.length == user.uid.length)
                    postsRepository.get(User(id))
                else
                    postsRepository.get(Topic(id))
            else
                MutableLiveData<List<Post>>().apply { value = mutableListOf() }
        }
    }

    fun get(post: Post) = postsRepository.get(post)

    fun setRate(view: View, post: Post, value: Int) {
        view.isEnabled = false
        ratingsRepository.rate(post, value) { view.isEnabled = true }
    }

    fun getRate(post: Post) = ratingsRepository.get(post)

    fun read(post: Post) = readingsRepository.read(post)

    fun share(view: View, post: Post) {
        view.isEnabled = false
        sharesRepository.share(post) { view.isEnabled = true }
    }

    fun bookmark(view: View, post: Post) {
        view.isEnabled = false
        bookmarksRepository.bookmark(post) {}
    }

    fun unBookmark(view: View, post: Post) {
        view.isEnabled = false
        bookmarksRepository.unBookmark(post) {}
    }

    fun isBookmarked(post: Post) = bookmarksRepository.isBookmarked(post)

    fun navigateUp(view: View) = view.findNavController().navigateUp()

}