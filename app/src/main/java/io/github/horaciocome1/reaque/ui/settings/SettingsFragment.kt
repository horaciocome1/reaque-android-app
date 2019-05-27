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

package io.github.horaciocome1.reaque.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        terms_and_conditions_textview.setOnClickListener(this::openLinks)
        privacy_policy_textview.setOnClickListener(this::openLinks)
        licenses_textview.setOnClickListener(this::openLinks)
        repository_textview.setOnClickListener(this::openLinks)
        donate_textview.setOnClickListener(this::openLinks)
        sign_out_textview.setOnClickListener {
            signOut()
        }
    }

    private fun openLinks(view: View) {
        val url = resources.getString(
            when (view) {
                terms_and_conditions_textview -> R.string.terms_and_conditions_url
                privacy_policy_textview -> R.string.privacy_policy_url
                licenses_textview -> R.string.licence_url
                else -> R.string.project_url
            }
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        activity.run {
            if (this is MainActivity)
                finish()
        }
    }

}