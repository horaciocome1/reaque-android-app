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
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    var userId = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_more_profile_button.setOnClickListener {
            if (userId != "") {
                val openProfile = MoreFragmentDirections.actionOpenProfileFromMore(userId)
                Navigation.findNavController(it).navigate(openProfile)
            }
        }
        fragment_more_settings_constraintlayout.setOnClickListener {
            val openSettings = MoreFragmentDirections.actionOpenSettings()
            Navigation.findNavController(it).navigate(openSettings)
        }
        fragment_more_feedback_constraintlayout.setOnClickListener {

        }
        fragment_more_about_constraintlayout.setOnClickListener {
            val url = resources.getString(R.string.read_me_url)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        hideButtons()
        viewModel.me.observe(this, Observer {
                Glide.with(this@MoreFragment)
                    .load(it.pic)
                    .apply(RequestOptions.circleCropTransform())
                    .into(fragment_more_profile_pic_imageview)
            fragment_more_name_textview.text = it.name
            if (it.id != "") {
                userId = it.id
                showButtons()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
    }

    private fun hideButtons() = View.GONE.run {
        fragment_more_profile_button.visibility = this
        fragment_more_edit_button.visibility = this
        fragment_more_logout_button.visibility = this
    }

    private fun showButtons() = View.VISIBLE.run {
        fragment_more_profile_button.visibility = this
        fragment_more_edit_button.visibility = this
        fragment_more_logout_button.visibility = this
    }

}