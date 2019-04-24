package io.github.horaciocome1.reaque.util

import android.content.res.Configuration
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.data.topics.Topic
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.ui.notifications.NotificationsAdapter
import io.github.horaciocome1.reaque.ui.notifications.NotificationsFragmentDirections
import io.github.horaciocome1.reaque.ui.posts.PostsAdapter
import io.github.horaciocome1.reaque.ui.posts.PostsFragmentDirections
import io.github.horaciocome1.reaque.ui.topics.TopicsAdapter
import io.github.horaciocome1.reaque.ui.users.UsersAdapter
import io.github.horaciocome1.reaque.ui.users.UsersFragmentDirections
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import jp.wasabeef.glide.transformations.BlurTransformation

class BindingAdapters {

    class LoadImageFromUrlOrUri {

        companion object {

            @BindingAdapter("url", "uri", "type", requireAll = false)
            @JvmStatic
            fun ImageView.loadImage(url: String?, uri: Uri?, type: Int?) {
                Glide.with(context).load(
                    if (uri != null && uri != Uri.EMPTY)
                        uri
                    else
                        url
                )
                    .apply(
                        when (type) {
                            Constants.BLUR -> RequestOptions.bitmapTransform(BlurTransformation(7, 14))
                            Constants.CIRCLE -> RequestOptions.circleCropTransform()
                            else -> RequestOptions()
                        }
                    )
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }

        }

    }

    class LoadList {

        companion object {

            @Suppress("UNCHECKED_CAST")
            @BindingAdapter("list", "orientation", "type", requireAll = false)
            @JvmStatic
            fun RecyclerView.loadList(list: List<Any>?, orientation: Int?, type: Int?) {
                list?.let {
                    when {
                        it.isListOfTopics -> loadTopics(it as List<Topic>, orientation!!, type!!)
                        it.isListOfPosts -> loadPosts(it as List<Post>)
                        it.isListOfUsers -> loadUsers(it as List<User>)
                        it.isListOfNotifications -> loadNotifications(it as List<Notification>)
                    }
                }
            }

            private fun RecyclerView.loadTopics(topics: List<Topic>, orientation: Int, type: Int) {
                layoutManager = LinearLayoutManager(
                    context,
                    when (orientation) {
                        Configuration.ORIENTATION_PORTRAIT -> RecyclerView.HORIZONTAL
                        else -> RecyclerView.VERTICAL
                    },
                    false
                )
                adapter = when (type) {
                    Constants.SIMPLE -> TopicsAdapter.Simple(topics)
                    else -> {
                        setItemViewCacheSize(topics.size)
                        TopicsAdapter(topics)
                    }
                }
            }

            private fun RecyclerView.loadPosts(posts: List<Post>) {
                layoutManager = LinearLayoutManager(context)
                adapter = PostsAdapter(posts)
                addOnItemClickListener { view, position ->
                    if (posts.isNotEmpty()) {
                        val directions = PostsFragmentDirections.actionOpenReadFromPosts(posts[position].id)
                        view.findNavController().navigate(directions)
                    }
                }
            }

            private fun RecyclerView.loadUsers(users: List<User>) {
                layoutManager = StaggeredGridLayoutManager(
                    when {
                        users.size >= Constants.TWO_COLUMNS -> Constants.TWO_COLUMNS
                        else -> Constants.SINGLE_COLUMN
                    },
                    RecyclerView.VERTICAL
                )
                adapter = UsersAdapter(users)
                addOnItemClickListener { view, position ->
                    if (users.isNotEmpty()) {
                        val directions = UsersFragmentDirections.actionOpenProfileFromUsers(users[position].id)
                        view.findNavController().navigate(directions)
                    }
                }
            }

            private fun RecyclerView.loadNotifications(notifications: List<Notification>) {
                layoutManager = LinearLayoutManager(context)
                adapter = NotificationsAdapter(notifications)
                addOnItemClickListener { view, position ->
                    if (notifications.isNotEmpty())
                        notifications[position].run {
                            val directions = when {
                                isUser -> NotificationsFragmentDirections.actionOpenProfileFromNotifications(
                                    contentId
                                )
                                isPost -> NotificationsFragmentDirections.actionOpenReadFromNotifications(
                                    contentId
                                )
                                else -> null
                            }
                            view.findNavController().navigate(directions!!)
                        }
                }
            }

        }

    }

}