package io.github.horaciocome1.reaque.util

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.ui.MainActivity

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

fun MainActivity.handleDynamicLinks(onSuccess: (Post) -> Unit) {
    FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
        .addOnSuccessListener { pendingDynamicLinkData ->
            pendingDynamicLinkData?.let {
                val postId: String = it.link.toString().removePrefix("${Constants.LANDING_PAGE}/")
                val post = Post(postId)
                onSuccess(post)
            }
        }
        .addOnFailureListener {
            Log.w("MainActivity", "getDynamicLink:Failure", it)
        }
}