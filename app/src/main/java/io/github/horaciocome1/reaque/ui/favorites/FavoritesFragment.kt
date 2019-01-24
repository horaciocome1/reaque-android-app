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

package io.github.horaciocome1.reaque.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomappbar.BottomAppBar
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.MainActivity
import io.github.horaciocome1.reaque.ui.menu.fragmentManager
import kotlinx.android.synthetic.main.fragment_favorites.*

fun FragmentManager.loadFavorites() {
    beginTransaction().replace(R.id.activity_main_container, FavoritesFragment()).commit()
    fragmentManager = this
}

class FavoritesFragment : Fragment() {

    private lateinit var bottomAppBarShadow: View
    private lateinit var bottomAppBar: BottomAppBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bottomAppBarShadow = (activity as MainActivity).findViewById(R.id.activity_main_bottomappbar_shadow)
        bottomAppBar = (activity as MainActivity).findViewById(R.id.activity_main_bottomappbar)
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomAppBarShadow.visibility = View.VISIBLE
        bottomAppBar.visibility = View.VISIBLE
    }

    override fun onStart() {
        super.onStart()
        fragment_favorites_viewpager.adapter = TabAdapter(fragmentManager)
        fragment_favorites_tablayout.setupWithViewPager(fragment_favorites_viewpager)
    }

    override fun onStop() {
        super.onStop()
        bottomAppBarShadow.visibility = View.GONE
        bottomAppBar.visibility = View.GONE
    }

}