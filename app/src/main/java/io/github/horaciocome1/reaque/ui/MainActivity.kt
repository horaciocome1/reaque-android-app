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

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.util.handleDynamicLinks
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private var passedThroughSignIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        auth = FirebaseAuth.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        handleDynamicLinks {
            val bundle = Bundle().apply {
                putString("post_id", it.id)
            }
            navController.navigate(R.id.destination_read, bundle)
        }
        setupNavigation()
    }

    override fun onStart() {
        super.onStart()
        passedThroughSignIn = if (auth.currentUser == null) {
            navController.navigate(R.id.destination_sign_in)
            false
        } else
            false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, activity_main_drawerlayout)

    private fun setupNavigation() {
        setupBottomNavigationMenu()
        setupSideNavigationMenu()
        setupActionBar()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomnavigationview?.visibility = when (destination.id) {
                R.id.destination_posting -> View.GONE
                R.id.destination_edit_profile -> View.GONE
                R.id.destination_sign_in -> View.GONE
                R.id.destination_viewer -> View.GONE
                else -> View.VISIBLE
            }
            supportActionBar?.run {
                when (destination.id) {
                    R.id.destination_posts -> if (isOrientationPortrait) hide() else show()
                    R.id.destination_users -> if (isOrientationPortrait) hide() else show()
                    R.id.destination_notifications -> if (isOrientationPortrait) hide() else show()
                    R.id.destination_more -> if (isOrientationPortrait) hide() else show()
                    R.id.destination_sign_in -> if (isOrientationPortrait) hide() else show()
                    R.id.destination_edit_profile -> hide()
                    R.id.destination_posting -> hide()
                    R.id.destination_viewer -> hide()
                    R.id.destination_profile -> title = ""
                    R.id.destination_read -> title = ""
                    else -> show()
                }
            }
            if (destination.id == R.id.destination_notifications && auth.currentUser == null && passedThroughSignIn) {
                passedThroughSignIn = false
                finish()
            }
        }
    }

    private fun setupBottomNavigationMenu() = bottomnavigationview?.let {
        NavigationUI.setupWithNavController(it, navController)
    }

    private fun setupSideNavigationMenu() = navigationview?.let {
        NavigationUI.setupWithNavController(it, navController)
    }

    private fun setupActionBar() =
        NavigationUI.setupActionBarWithNavController(this, navController, activity_main_drawerlayout)

    private val isOrientationPortrait: Boolean
        get() {
            return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        }

}
