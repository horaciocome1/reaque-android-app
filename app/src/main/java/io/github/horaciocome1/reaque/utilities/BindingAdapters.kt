package io.github.horaciocome1.reaque.utilities

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
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


}