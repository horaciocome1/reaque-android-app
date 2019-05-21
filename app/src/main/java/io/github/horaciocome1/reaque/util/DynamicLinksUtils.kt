package io.github.horaciocome1.reaque.util

import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.posts.PostsFragmentDirections

fun FirebaseDynamicLinks.buildShortDynamicLink(post: Post): Task<ShortDynamicLink> {
    return createDynamicLink()
        .setLink(Uri.parse("${Constants.LANDING_PAGE}/${post.id}"))
        .setDomainUriPrefix("https://reaque.page.link")
        .setAndroidParameters(
            DynamicLink.AndroidParameters.Builder()
                .setFallbackUrl(Uri.parse(Constants.LANDING_PAGE))
                .build()
        )
        .setIosParameters(
            DynamicLink.IosParameters.Builder("")
                .setFallbackUrl(Uri.parse(Constants.LANDING_PAGE))
                .setIpadFallbackUrl(Uri.parse(Constants.LANDING_PAGE))
                .build()
        )
        .setSocialMetaTagParameters(
            DynamicLink.SocialMetaTagParameters.Builder()
                .setTitle("${post.title} - ${post.user.name}")
                .setDescription(post.message)
                .setImageUrl(Uri.parse(post.pic))
                .build()
        )
        .buildShortDynamicLink()
}

fun MainActivity.handleDynamicLinks(controller: NavController) {
    FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
        .addOnSuccessListener { pendingDynamicLinkData ->
            pendingDynamicLinkData?.let {
                val postId: String = it.link.toString().removePrefix("${Constants.LANDING_PAGE}/")
                val directions = PostsFragmentDirections.actionOpenReadFromPosts(postId)
                controller.navigate(directions)
            }
        }
        .addOnFailureListener {
            Log.w("MainActivity", "getDynamicLink:Failure", it)
        }
}