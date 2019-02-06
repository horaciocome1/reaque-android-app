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

package io.github.horaciocome1.reaque.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.ui.signin.getSignInActivityIntent
import kotlinx.android.synthetic.main.activity_main.*

var firstInit = true
lateinit var myFragmentManager: FragmentManager

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myFragmentManager = supportFragmentManager
        if (firstInit)
            startActivityForResult(getSignInActivityIntent(), 101)
        firstInit = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.TRANSPARENT
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setSupportActionBar(activity_main_toolbar)
        setupBottomNavigationMenu()
        setupSideNavigationMenu()
        setupActionBar()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, activity_main_drawerlayout)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode != Activity.RESULT_OK)
            finish()
    }

    private fun setupBottomNavigationMenu() = activity_main_bottomnavigationview?.let {
        NavigationUI.setupWithNavController(it, navController)
    }

    private fun setupSideNavigationMenu() = activity_main_navigationview?.let {
        NavigationUI.setupWithNavController(it, navController)
    }

    private fun setupActionBar() =
        NavigationUI.setupActionBarWithNavController(this, navController, activity_main_drawerlayout)



}
