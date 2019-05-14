/*
 *    Copyright 2019 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reaque.ui.posts

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import io.github.horaciocome1.reaque.data.posts.Post
import io.github.horaciocome1.reaque.databinding.FragmentReadBinding
import kotlinx.android.synthetic.main.fragment_read.*

class ReadFragment: Fragment() {

    private lateinit var binding: FragmentReadBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private lateinit var dynamicLinks: FirebaseDynamicLinks

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dynamicLinks = FirebaseDynamicLinks.getInstance()
        binding = FragmentReadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(fragment_read_bottom_sheet).apply {
            state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        share_button.setOnClickListener {
            it.isEnabled = false
            buildAndSendDynamicLink(binding.post!!)
            it.isEnabled = true
        }
    }

    override fun onStart() {
        super.onStart()
        binding.let {
            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }
        arguments?.let { bundle ->
            val post = Post(ReadFragmentArgs.fromBundle(bundle).postId)
            viewModel.getPosts(post).observe(this, Observer {
                binding.post = it
            })
            viewModel.isThisFavoriteForMe(post).observe(this, Observer {
                add_to_favorites_button.visibility = if (it) View.GONE else View.VISIBLE
                remove_from_favorites_button.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    private fun buildAndSendDynamicLink(post: Post) {
        dynamicLinks.createDynamicLink()
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
            .addOnSuccessListener {
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, it.shortLink.toString())
                    type = "text/plain"
                }
                val chooser = Intent.createChooser(sendIntent, "Partilhar - ${post.title}")
                startActivity(chooser)
            }
            .addOnFailureListener {
                Log.e(tag, "Failed to build short link", it)
            }
    }

}