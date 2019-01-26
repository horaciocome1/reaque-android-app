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
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_settings_terms_and_conditions.setOnClickListener(this::onUrlOnClickListener)
        fragment_settings_privacy_policy.setOnClickListener(this::onUrlOnClickListener)
        fragment_settings_licenses.setOnClickListener(this::onUrlOnClickListener)
        fragment_settings_documentation.setOnClickListener(this::onUrlOnClickListener)
        fragment_settings_project_page.setOnClickListener(this::onUrlOnClickListener)
    }

    private fun onUrlOnClickListener(view: View) {
        val url = resources.getString(
            when (view) {
                fragment_settings_terms_and_conditions -> R.string.terms_and_conditions_url
                fragment_settings_privacy_policy -> R.string.privacy_policy_url
                fragment_settings_licenses -> R.string.licence_url
                fragment_settings_documentation -> R.string.read_me_url
                fragment_settings_project_page -> R.string.project_url
                else -> Log.w(tag, "View not found!")
            }
        )
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
    }

}