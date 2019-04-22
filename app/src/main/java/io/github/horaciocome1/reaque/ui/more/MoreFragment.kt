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
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.databinding.FragmentMoreBinding
import io.github.horaciocome1.reaque.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment : Fragment() {

    lateinit var binding: FragmentMoreBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedback_textview.setOnClickListener {

        }
        about_textview.setOnClickListener {
            openReadMe()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.viewmodel = viewModel
        viewModel.me.observe(this, Observer {
            binding.user = it
        })
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.hide()
    }

    private fun openReadMe() {
        val url = resources.getString(R.string.read_me_url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}