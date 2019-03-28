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

package io.github.horaciocome1.reaque.ui.more

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        post_textview.setOnClickListener {
            val openPost = MoreFragmentDirections.actionOpenPosting()
            Navigation.findNavController(it).navigate(openPost)
        }
        setting_textview.setOnClickListener {
            val openSettings = MoreFragmentDirections.actionOpenSettings()
            Navigation.findNavController(it).navigate(openSettings)
        }
        feedback_textview.setOnClickListener {

        }
        about_textview.setOnClickListener {
            val url = resources.getString(R.string.read_me_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.me.observe(this, Observer { user ->
            Glide.with(this@MoreFragment)
                .load(user.pic)
                    .apply(RequestOptions.circleCropTransform())
                    .into(profile_pic_imageview)
            name_textview.text = user.name
            profile_pic_imageview.setOnClickListener {
                val openProfile = MoreFragmentDirections.actionOpenProfileFromMore(user.id)
                Navigation.findNavController(it).navigate(openProfile)
            }
            logout_button.setOnClickListener {
                auth = FirebaseAuth.getInstance()
                auth.signOut()
                (activity as MainActivity).finish()

            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
    }

}