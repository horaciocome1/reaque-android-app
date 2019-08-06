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
import android.net.Uri
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

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel
        licenses_textview.setOnClickListener {
            val url = resources.getString(R.string.url_licence)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        about_textview.setOnClickListener {
            val url = resources.getString(R.string.url_about)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        privacy_policy_textview.setOnClickListener {
            val url = resources.getString(R.string.url_privacy_policy)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        terms_and_conditions_textview.setOnClickListener {
            val url = resources.getString(R.string.url_terms_and_conditions)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
        feedback_textview.setOnClickListener {
            val email = resources.getString(R.string.email_developer)
            try {
                val mailto = "mailto:$email"
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse(mailto)
                startActivity(emailIntent)
            } catch (exception: Exception) {
                Toast.makeText(it.context, R.string.email_app_not_found, Toast.LENGTH_LONG).show()
            }
        }
        sign_out_textview.setOnClickListener {
            auth.signOut()
            activity?.finish()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.user.observe(this, Observer { binding.user = it })
        viewModel.hasBookmarks.observe(this, Observer {
            val visibility = if (it)
                View.VISIBLE
            else
                View.GONE
            bookmarks_textview.visibility = visibility
            bookmarks_imageview.visibility = visibility
        })
    }

}