package io.github.horaciocome1.reaque.ui._posts

import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.data.bookmarks.BookmarksRepository
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.ratings.RatingsRepository
import io.github.horaciocome1.reaque.data.readings.ReadingsRepository
import io.github.horaciocome1.reaque.data.shares.SharesRepository
import io.github.horaciocome1.reaque.data.storage.StorageRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.ObservableViewModel

class PostsViewModel(
    private val postsRepository: PostsRepository,
    private val storageRepository: StorageRepository,
    topicsRepository: TopicsRepository,
    private val readingsRepository: ReadingsRepository,
    private val sharesRepository: SharesRepository,
    private val ratingsRepository: RatingsRepository,
    private val bookmarksRepository: BookmarksRepository
) : ObservableViewModel() {

    val post = Post("").apply {
        topic.title = "Selecione um tópico."
    }

    var imageUri: Uri = Uri.EMPTY

    @Bindable
    val title = MutableLiveData<String>()

    @Bindable
    val message = MutableLiveData<String>()

    val isCreatingPost = MutableLiveData<Boolean>().apply { value = false }

    val topics = topicsRepository.topics

    private lateinit var posts: LiveData<List<Post>>

    fun create(view: View) {
        isCreatingPost.value = true
        storageRepository.uploadImage(imageUri, post.topic) { url ->
            post.pic = url
            postsRepository.create(post) { _ ->
                view.context?.let {
                    Toast.makeText(it, "Post created.", Toast.LENGTH_LONG).show()
                }
            }
        }
        navigateUp(view)
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

    fun rate(view: View, post: Post, value: Int) {
        view.isEnabled = false
        ratingsRepository.rate(post, value) { view.isEnabled = true }
    }

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