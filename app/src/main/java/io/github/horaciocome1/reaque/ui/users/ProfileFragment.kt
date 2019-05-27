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

package io.github.horaciocome1.reaque.ui.users

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment: Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        behavior = BottomSheetBehavior.from(posts_bottomsheet)
        binding.let {
            it.lifecycleOwner = this
            it.viewmodel = viewModel
        }
        posts_button.setOnClickListener {
            binding.user?.run {
                if (id.isNotBlank())
                    showPosts()
            }
        }
        send_email_button.setOnClickListener {
            sendEmail()
        }
    }

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        arguments?.let { args ->
            val user = User(ProfileFragmentArgs.fromBundle(args).userId)
            viewModel.getUsers(user).observe(this, Observer {
                binding.user = it
            })
            viewModel.isThisFavoriteForMe(user).observe(this, Observer {
                add_to_favorites_button.visibility = if (it) View.GONE else View.VISIBLE
                remove_from_favorites_button.visibility = if (it) View.VISIBLE else View.GONE
            })
        }
    }

    private fun User.showPosts() {
        viewModel.getPosts(this).observe(this@ProfileFragment, Observer {
            binding.posts = it
            behavior.run {
                state = BottomSheetBehavior.STATE_HALF_EXPANDED
                skipCollapsed = true
            }
        })
    }

    private fun sendEmail() {
        val email = binding.user?.email!!
        val mailto = "mailto:$email"
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse(mailto)
        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            Snackbar.make(root_view!!, R.string.email_app_not_found, Snackbar.LENGTH_LONG).show()
        }
    }

}