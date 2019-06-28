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
        setupNavigation()
        handleDynamicLinks {
            val bundle = Bundle().apply {
                putString("post_id", it.id)
            }
//            navController.navigate(R.id.destination_read, bundle)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null)
            navController.navigate(R.id.destination_sign_in)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, drawerlayout)

    private fun setupNavigation() {
        bottomnavigationview?.let { NavigationUI.setupWithNavController(it, navController) }
        navigationview?.let { NavigationUI.setupWithNavController(it, navController) }
        NavigationUI.setupActionBarWithNavController(this, navController, drawerlayout)
        navController.addOnDestinationChangedListener(onDestinationChangedListener)
    }

    private val isOrientationPortrait: Boolean
        get() {
            return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        }

    private val onDestinationChangedListener = NavController.OnDestinationChangedListener { _, destination, _ ->
        if (destination.id != R.id.destination_sign_in && auth.currentUser == null && passedThroughSignIn) {
            passedThroughSignIn = false
            finish()
        }
        when (destination.id) {
            R.id.destination_sign_in -> {
                supportActionBar?.hide()
                bottomnavigationview?.visibility = View.GONE
            }
            R.id.destination_feed -> supportActionBar?.hide()
            R.id.destination_explore -> supportActionBar?.hide()
            R.id.destination_more -> supportActionBar?.hide()
            else -> {
                supportActionBar?.show()
                bottomnavigationview?.visibility = View.VISIBLE
            }
        }
    }

}
