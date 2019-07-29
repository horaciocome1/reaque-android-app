package io.github.horaciocome1.reaque.ui.explore

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.posts.PostsRepository
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.topics.TopicsRepository
import io.github.horaciocome1.reaque.util.Constants

class ExploreViewModel(topicsRepository: TopicsRepository, postsRepository: PostsRepository) : ViewModel() {

    var topics: List<Topic> = mutableListOf()

    var posts: List<Post> = mutableListOf()

    val notEmptyTopics = topicsRepository.notEmptyTopics

    val top10 = postsRepository.getTop10()

    val onItemPostClickListener: (View, Int) -> Unit = { view, position ->
        if (posts.isNotEmpty()) {
            val directions = ExploreFragmentDirections.actionOpenReadPostFromExplore(posts[position].id)
            view.findNavController().navigate(directions)
        }
    }

    val onItemTopicClickListener: (View, Int) -> Unit = { view, position ->
        if (posts.isNotEmpty()) {
            val directions =
                ExploreFragmentDirections.actionOpenPostsFromExplore(topics[position].id, Constants.TOPIC_POSTS_REQUEST)
            view.findNavController().navigate(directions)
        }
    }

    val onItemTopicLongPressListener: (View, Int) -> Unit = { view, position ->
        if (posts.isNotEmpty()) {
            val directions =
                ExploreFragmentDirections.actionOpenUsersFromExplore(topics[position].id, Constants.TOPIC_USERS_REQUEST)
            view.findNavController().navigate(directions)
        }
    }

    fun setTopics(topics: List<Topic>): ExploreViewModel {
        this.topics = topics
        return this
    }

    fun setPosts(posts: List<Post>): ExploreViewModel {
        this.posts = posts
        return this
    }

}