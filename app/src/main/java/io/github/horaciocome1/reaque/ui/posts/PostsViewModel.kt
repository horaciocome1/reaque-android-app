package io.github.horaciocome1.reaque.ui.posts

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
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

    fun getRating(post: Post) = ratingsRepository.get(post)

    fun setRate(view: View, post: Post, value: Int) {
        navigateUp(view)
        ratingsRepository.rate(post, value) { }
    }

    fun read(post: Post) = readingsRepository.read(post)

    fun share(fragment: ReadPostFragment, view: View, post: Post) {
        view.isEnabled = false
        dynamicLinks.buildShortDynamicLink(post) {
            fragment.startActivity(it)
            sharesRepository.share(post) { view.isEnabled = true }
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

    fun navigateUp(view: View) = view.findNavController().navigateUp()

    fun openUserProfile(view: View, user: User) {
        val directions = ReadPostFragmentDirections.actionOpenUserProfileFromReadPost(user.id)
        view.findNavController().navigate(directions)
    }

}