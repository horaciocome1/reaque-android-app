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

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.github.horaciocome1.reaque.databinding.FragmentPostBinding
import io.github.horaciocome1.reaque.ui.MainActivity

class PostFragment : Fragment() {

    lateinit var binding: FragmentPostBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            (activity as MainActivity).supportActionBar?.show()
    }

    override fun onStart() {
        super.onStart()
        binding.apply {
            lifecycleOwner = this@PostFragment
            this.viewmodel = viewModel
        }
        viewModel.postTitle.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

}