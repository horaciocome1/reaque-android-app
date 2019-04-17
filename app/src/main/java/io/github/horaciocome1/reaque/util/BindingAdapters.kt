package io.github.horaciocome1.reaque.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import io.github.horaciocome1.reaque.data.notifications.Notification
import io.github.horaciocome1.reaque.ui.notifications.NotificationsAdapter
import io.github.horaciocome1.reaque.ui.notifications.NotificationsFragmentDirections
import io.github.horaciocome1.simplerecyclerviewtouchlistener.addOnItemClickListener
import jp.wasabeef.glide.transformations.BlurTransformation

class BindingAdapters {

    class LoadImageFromUrl {

        companion object {

            @BindingAdapter("url", "type")
            @JvmStatic
            fun ImageView.loadImage(url: String?, type: Int?) {
                Glide.with(context).load(url)
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

    class LoadImageFromUri {

        companion object {

            @BindingAdapter("uri", "type")
            @JvmStatic
            fun ImageView.loadImage(uri: Uri?, type: Int?) {
                Glide.with(context).load(uri)
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

    class LoadImageFromUrlOrUri {

        companion object {

            @BindingAdapter("url", "uri", "type")
            @JvmStatic
            fun ImageView.loadImage(url: String?, uri: Uri?, type: Int?) {
                Glide.with(context).load(
                    if (uri != Uri.EMPTY)
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

    class LoadNotificationList {

        companion object {

            @BindingAdapter("notifications")
            @JvmStatic
            fun RecyclerView.loadList(notifications: List<Notification>?) {
                notifications?.let {
                    layoutManager = LinearLayoutManager(context)
                    adapter = NotificationsAdapter(it)
                    if (!hasOnClickListeners())
                        addOnItemClickListener { view, position ->
                            it[position].run {
                                val directions = when {
                                    isUser -> NotificationsFragmentDirections.actionOpenProfileFromNotifications(
                                        contentId
                                    )
                                    isPost -> NotificationsFragmentDirections.actionOpenReadFromNotifications(contentId)
                                    else -> null
                                }
                                view.findNavController().navigate(directions!!)
                            }
                        }
                }
            }

        }

    }

}