package io.github.horaciocome1.reaque.ui.posts.create

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.storage.StorageRepository
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.util.Constants
import io.github.horaciocome1.reaque.util.ObservableViewModel

class CreatePostViewModel(
    topicsRepository: TopicsRepository,
    private val postsRepository: PostsRepository,
    private val storageRepository: StorageRepository
) : ObservableViewModel() {

    val post = Post("").apply {
        topic.title = "Selecione um tópico."
    }
    var imageUri: Uri = Uri.EMPTY

    @Bindable
    val title = MutableLiveData<String>()

    @Bindable
    val message = MutableLiveData<String>()

    val topics = topicsRepository.topics

    var isCreatingPost = false

    val isPostReady: Boolean
        get() {
            post.run {
                return title.isNotBlank()
                        && message.isNotBlank()
                        && topic.id.isNotBlank()
                        && imageUri != Uri.EMPTY
            }
        }

    val navigateUp: (View) -> Unit = {
        it.findNavController()
            .navigateUp()
    }

    fun create(view: View): CreatePostViewModel {
        isCreatingPost = true
        storageRepository.uploadImage(imageUri, post.topic) { uri: Uri? ->
            uri?.let {
                post.pic = it.toString()
                postsRepository.create(post) {
                    navigateUp(view)
                }
            }
        }
        return this
    }

    fun saveDraft(activity: FragmentActivity) {
        val sharedPreferences = activity.getSharedPreferences(
            Constants.SharedPreferences.NAME,
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit()
            .apply {
                putString(Constants.SharedPreferences.POST_TITLE, post.title)
                putString(Constants.SharedPreferences.POST_MESSAGE, post.message)
                apply()
            }
    }

    fun loadDraft(activity: FragmentActivity) {
        val sharedPreferences = activity.getSharedPreferences(
            Constants.SharedPreferences.NAME,
            Context.MODE_PRIVATE
        )
        sharedPreferences.getString(Constants.SharedPreferences.POST_TITLE, "")
            ?.let {
                if (it.isNotBlank())
                    title.value = it
            }
        sharedPreferences.getString(Constants.SharedPreferences.POST_MESSAGE, "")
            ?.let {
                if (it.isNotBlank())
                    message.value = it
            }
    }

}