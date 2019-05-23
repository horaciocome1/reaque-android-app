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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.databinding.FragmentMoreBinding
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedback_textview.setOnClickListener {
            emailDeveloper()
        }
        frequently_asked_questions_textview.setOnClickListener(this::openLinks)
        about_textview.setOnClickListener(this::openLinks)
    }

    override fun onStart() {
        super.onStart()
        binding.viewmodel = viewModel
        viewModel.me.observe(this, Observer { user ->
            binding.user = user
        })
    }

    private fun openLinks(view: View) {
        val link = when (view) {
            frequently_asked_questions_textview -> resources.getString(R.string.project_url)
            about_textview -> resources.getString(R.string.read_me_url)
            else -> null
        }
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }

    private fun emailDeveloper() {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        val email = resources.getString(R.string.developer_email)
        val subject = "Feedback - Reaque Android App"
        val mailto = "mailto:$email&subject=${Uri.encode(subject)}"
        emailIntent.data = Uri.parse(mailto)
        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            Snackbar.make(view!!, R.string.email_app_not_found, Snackbar.LENGTH_LONG).show()
        }
    }

}