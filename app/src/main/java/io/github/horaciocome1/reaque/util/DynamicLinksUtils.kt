package io.github.horaciocome1.reaque.util

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ShortDynamicLink
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.posts.PostsFragmentDirections

fun FirebaseDynamicLinks.buildShortDynamicLinc(post: Post): Task<ShortDynamicLink> {
    val dynamicLinks = FirebaseDynamicLinks.getInstance()
    return dynamicLinks.createDynamicLink()
        .setLink(Uri.parse("https://www.reaque.firebase.com/${post.id}"))
        .setDomainUriPrefix("https://reaque.page.link")
        .setAndroidParameters(
            DynamicLink.AndroidParameters.Builder()
                .setFallbackUrl(Uri.parse("https://www.reaque.firebase.com"))
                .build()
        )
        .setIosParameters(
            DynamicLink.IosParameters.Builder("")
                .setFallbackUrl(Uri.parse("https://www.reaque.firebase.com"))
                .build()
        )
        .setSocialMetaTagParameters(
            DynamicLink.SocialMetaTagParameters.Builder()
                .setTitle("${post.title} - ${post.user.name}")
                .setDescription(
                    "${post.message.substring(
                        0,
                        if (post.message.length >= 155) 155 else post.message.length
                    )} . . . "
                )
                .setImageUrl(Uri.parse(post.pic))
                .build()
        )
        .buildShortDynamicLink()
}

fun MainActivity.handleDynamicLinks(controller: ) {
    FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
        .addOnSuccessListener { pendingDynamicLinkData ->
            pendingDynamicLinkData?.let {
                val postId: String = it.link.toString().removePrefix("https://www.reaque.firebase.com/")
                val directions = PostsFragmentDirections.actionOpenReadFromPosts(postId)
                navController.navigate(directions)
            }
        }
        .addOnFailureListener {
            Log.w(tag, "getDynamicLink:Failure", it)
        }
}