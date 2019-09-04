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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.databinding.FragmentMoreBinding
import io.github.horaciocome1.reaque.util.InjectorUtils
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding

    private val viewModel: MoreViewModel by lazy {
        val factory = InjectorUtils.moreViewModelFactory
        ViewModelProviders.of(this, factory)[MoreViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        licenses_textview.setOnClickListener {
            val url = getString(R.string.url_licence)
            viewModel.openUrl(this, url)
        }
        about_textview.setOnClickListener {
            val url = getString(R.string.url_about)
            viewModel.openUrl(this, url)
        }
        privacy_policy_textview.setOnClickListener {
            val url = getString(R.string.url_privacy_policy)
            viewModel.openUrl(this, url)
        }
        terms_and_conditions_textview.setOnClickListener {
            val url = getString(R.string.url_terms_and_conditions)
            viewModel.openUrl(this, url)
        }
        feedback_textview.setOnClickListener { textView ->
            val email = getString(R.string.email_developer)
            viewModel.sendEmail(this, email) {
                val message = R.string.email_app_not_found
                Toast.makeText(textView.context, message, Toast.LENGTH_LONG)
                    .show()
            }
        }
        sign_out_textview.setOnClickListener {
            val auth = FirebaseAuth.getInstance()
            auth.signOut()
            activity?.finish()
        }
        update_textview.setOnClickListener {
            val url = getString(R.string.url_update)
            viewModel.openUrl(this, url)
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.user
            .observe(this, Observer {
                binding.user = it
            })
        viewModel.hasBookmarks
            .observe(this, Observer {
                bookmarks_textview.visibility = visibility(it)
                bookmarks_imageview.visibility = visibility(it)
            })
        viewModel.isUpdateAvailable
            .observe(this, Observer { isUpdateAvailable ->
                update_available_cardview.visibility = visibility(isUpdateAvailable)
                if (isUpdateAvailable)
                    viewModel.getLatestVersionName
                        .observe(this, Observer {
                            update_textview.text = it
                        })
            })
    }

    private fun visibility(state: Boolean): Int {
        return if (state)
            View.VISIBLE
        else
            View.GONE
    }

}