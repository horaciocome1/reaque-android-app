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

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import io.github.horaciocome1.reaque.R
import io.github.horaciocome1.reaque.data.users.User
import io.github.horaciocome1.reaque.databinding.ActivityMainBinding
import io.github.horaciocome1.reaque.util.Constants.USER_ID
import io.github.horaciocome1.reaque.util.handleDynamicLinks
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private val navController: NavController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private var passedThroughSignIn = false

    private val isOrientationPortrait: Boolean
        get() {
            return resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(R.layout.activity_main)
        checkGoogleApiAvailability()
        setLightStatusBar()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupNavigation()
        handleNotifications {
            val bundle = Bundle().apply {
                putString("user_id", it.id)
            }
            navController.navigate(R.id.destination_user_profile, bundle)
        }
        handleDynamicLinks {
            val bundle = Bundle().apply {
                putString("post_id", it.id)
            }
            navController.navigate(R.id.destination_read_post, bundle)
        }
    }

    private fun handleNotifications(listener: (User) -> Unit) {
        val userId = intent.getStringExtra(USER_ID)
        userId?.let {
            if (it.isNotBlank()) {
                val user = User(it)
                listener(user)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        checkGoogleApiAvailability()
        if (auth.currentUser == null)
            navController.navigate(R.id.destination_sign_in)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.menu_about) {
                val url = resources.getString(R.string.url_about)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return super.onOptionsItemSelected(it)
            }
        }
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() = NavigationUI.navigateUp(navController, drawerlayout)

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        title_textview.text = destination.label
        if (destination.id == R.id.destination_sign_in)
            passedThroughSignIn = true
        if (
            destination.id != R.id.destination_sign_in
            && auth.currentUser == null
            && passedThroughSignIn
        ) {
            passedThroughSignIn = false
            finish()
        }
        if (isOrientationPortrait)
            supportActionBar?.run {
                when (destination.id) {
                    R.id.destination_feed -> hide()
                    R.id.destination_explore -> hide()
                    R.id.destination_more -> hide()
                    else -> show()
                }
            }
        if (
            destination.id == R.id.destination_set_rating
            || destination.id == R.id.destination_create_post
            || destination.id == R.id.destination_update_user
            || destination.id == R.id.destination_sign_in
        )
            supportActionBar?.hide()
        if (
            destination.id != R.id.destination_feed
            && destination.id != R.id.destination_explore
            && destination.id != R.id.destination_more
        ) {
            bottomnavigationview?.visibility = View.GONE
            divider6?.visibility = View.GONE
        } else {
            bottomnavigationview?.visibility = View.VISIBLE
            divider6?.visibility = View.VISIBLE
        }
    }

    private fun setupNavigation() {
        bottomnavigationview?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
        navigationview?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
        NavigationUI.setupActionBarWithNavController(this, navController, drawerlayout)
        navController.addOnDestinationChangedListener(this)
    }

    private fun checkGoogleApiAvailability() {
        val instance = GoogleApiAvailability.getInstance()
        val code = instance.isGooglePlayServicesAvailable(this)
        if (code != ConnectionResult.SUCCESS)
            instance.makeGooglePlayServicesAvailable(this)
    }

    private fun setLightStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

}
