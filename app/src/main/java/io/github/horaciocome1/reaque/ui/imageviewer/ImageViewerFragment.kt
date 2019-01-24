/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
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

package io.github.horaciocome1.reaque.ui.imageviewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.utilities.getGlide
import kotlinx.android.synthetic.main.fragment_image_viewer.*

lateinit var link: String

fun FragmentManager.viewPic(pic: String) {
    val fragment = ViewerFragment()
    beginTransaction().replace(R.id.activity_main_container, fragment)
        .addToBackStack(fragment.tag).commit()
    link = pic
}

class ViewerFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_image_viewer_back_button.setOnClickListener { activity?.onBackPressed() }
    }

    override fun onStart() {
        super.onStart()
        getGlide().load(link).into(fragment_image_viewer_imageview)
    }

}