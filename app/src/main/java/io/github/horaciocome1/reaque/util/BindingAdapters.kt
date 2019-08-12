package io.github.horaciocome1.reaque.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.explore.TopicsAdapter
import io.github.horaciocome1.reaque.ui.posts.PostsAdapter
import io.github.horaciocome1.reaque.ui.users.UsersAdapter
import jp.wasabeef.glide.transformations.BlurTransformation

class BindingAdapters {

    class LoadImageFromUrlOrUri {

        companion object {

            @BindingAdapter("url", "uri", "type", requireAll = false)
            @JvmStatic
            fun ImageView.loadImage(url: String?, uri: Uri?, type: Int?) {
                val image = if (uri != null && uri != Uri.EMPTY)
                    uri
                else
                    url
                val options = when (type) {
                    Constants.BLUR -> RequestOptions.bitmapTransform(BlurTransformation(7, 14))
                    Constants.CIRCLE -> RequestOptions.circleCropTransform()
                    else -> RequestOptions()
                }
                Glide.with(context)
                    .load(image)
                    .apply(options)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }

        }

    }

    @Suppress("UNCHECKED_CAST")
    class LoadList {

        companion object {

            @BindingAdapter("list", "type", "columns", "host")
            @JvmStatic
            fun RecyclerView.loadList(list: List<Any>?, type: Int?, columns: Int?, host: Int?) {
                if (list != null && type != null && columns != null && host != null)
                    when (type) {
                        Constants.LISTING_TOPICS -> {
                            val topics = list as List<Topic>
                            loadTopics(topics, columns)
                        }
                        Constants.LISTING_POSTS -> {
                            val posts = list as List<Post>
                            loadPosts(posts, columns, host)
                        }
                        Constants.LISTING_USERS -> {
                            val users = list as List<User>
                            loadUsers(users, columns)
                        }
                    }
            }

            private fun RecyclerView.loadTopics(list: List<Topic>, columns: Int) {
                layoutManager = if (columns == 1)
                    LinearLayoutManager(context)
                else
                    StaggeredGridLayoutManager(columns, RecyclerView.VERTICAL)
                adapter = TopicsAdapter(list)
            }

            private fun RecyclerView.loadPosts(list: List<Post>, columns: Int, host: Int) {
                layoutManager = when {
                    host == Constants.EXPLORE_FRAGMENT -> LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    columns == 1 -> LinearLayoutManager(context)
                    else -> StaggeredGridLayoutManager(columns, RecyclerView.VERTICAL)
                }
                adapter = if (host == Constants.EXPLORE_FRAGMENT) {
                    setItemViewCacheSize(10)
                    PostsAdapter.SuggestionsAdapter(list)
                } else
                    PostsAdapter(list)
            }

            private fun RecyclerView.loadUsers(list: List<User>, columns: Int) {
                layoutManager = if (columns == 1)
                    LinearLayoutManager(context)
                else
                    StaggeredGridLayoutManager(columns, RecyclerView.VERTICAL)
                adapter = UsersAdapter(list)
            }

        }

    }

}