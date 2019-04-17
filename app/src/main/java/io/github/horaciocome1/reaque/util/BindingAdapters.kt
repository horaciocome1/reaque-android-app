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

    class LoadNotifications {

        companion object {

            @BindingAdapter("notifications")
            @JvmStatic
            fun RecyclerView.load(notifications: List<Notification>?) {
                notifications?.let {
                    layoutManager = LinearLayoutManager(context)
                    adapter = NotificationsAdapter(it)
                    if (!hasOnClickListeners())
                        addOnItemClickListener { view, position ->
                            if (it.isNotEmpty())
                                it[position].run {
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

    class LoadTopics {

        companion object {

            @BindingAdapter("topics", "orientation")
            @JvmStatic
            fun RecyclerView.load(topics: List<Topic>?, orientation: Int?) {
                topics?.let {
                    layoutManager = LinearLayoutManager(
                            context,
                        when (orientation) {
                            Configuration.ORIENTATION_PORTRAIT -> RecyclerView.HORIZONTAL
                            else -> RecyclerView.VERTICAL
                        },
                            false
                        )
                    adapter = TopicsAdapter(it)
                }
            }

        }

    }

    class LoadPosts {

        companion object {

            @BindingAdapter("posts")
            @JvmStatic
            fun RecyclerView.load(posts: List<Post>?) {
                posts?.let {
                    layoutManager = LinearLayoutManager(context)
                    adapter = PostsAdapter(it)
                    if (!hasOnClickListeners())
                        addOnItemClickListener { view, position ->
                            if (it.isNotEmpty()) {
                                val directions = PostsFragmentDirections.actionOpenReadFromPosts(it[position].id)
                                view.findNavController().navigate(directions)
                            }
                        }
                }
            }

        }

    }

    class LoadUsers {

        companion object {

            @BindingAdapter("users")
            @JvmStatic
            fun RecyclerView.load(users: List<User>?) {
                users?.let {
                    layoutManager = StaggeredGridLayoutManager(
                        when {
                            it.size >= Constants.TWO_COLUMNS -> Constants.TWO_COLUMNS
                            else -> Constants.SINGLE_COLUMN
                        },
                        RecyclerView.VERTICAL
                    )
                    adapter = UsersAdapter(it)
                    if (!hasOnClickListeners())
                        addOnItemClickListener { view, position ->
                            if (it.isNotEmpty()) {
                                val directions = UsersFragmentDirections.actionOpenProfileFromUsers(it[position].id)
                                view.findNavController().navigate(directions)
                            }
                        }
                }
            }

        }

    }

}