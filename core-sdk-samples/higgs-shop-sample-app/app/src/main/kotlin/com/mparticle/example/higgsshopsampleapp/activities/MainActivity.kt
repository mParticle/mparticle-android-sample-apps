package com.mparticle.example.higgsshopsampleapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mparticle.MPEvent
import com.mparticle.MParticle
import com.mparticle.example.higgsshopsampleapp.R
import com.mparticle.example.higgsshopsampleapp.databinding.ActivityMainBinding
import com.mparticle.example.higgsshopsampleapp.utils.Constants


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var activityResultLaunch: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_mParticle_SampleApp)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_shop,
                R.id.navigation_account,
                R.id.navigation_cart
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigation.setupWithNavController(navController)
        bottomNavigation.setOnItemSelectedListener { item ->
            val customAttributes: MutableMap<String, String> = HashMap()
            when (item.itemId) {
                R.id.navigation_shop -> {
                    customAttributes["destination"] = getString(R.string.nav_shop)
                    navController.navigate(R.id.navigation_shop)
                }
                R.id.navigation_account -> {
                    customAttributes["destination"] = getString(R.string.nav_account)
                    navController.navigate(R.id.navigation_account)
                }
                R.id.navigation_cart -> {
                    customAttributes["destination"] = getString(R.string.nav_cart)
                    navController.navigate(R.id.navigation_cart)
                }
            }
            val event = MPEvent.Builder("Navbar Clicked", MParticle.EventType.Navigation)
                .customAttributes(customAttributes)
                .build()
            MParticle.getInstance()?.logEvent(event)
            return@setOnItemSelectedListener true
        }

        activityResultLaunch =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult()
            ) { result ->
                when (result.resultCode) {
                    Constants.RESULT_CODE_CART_ADDED -> {
                        navController.navigate(R.id.navigation_cart)
                    }
                    Constants.RESULT_CODE_PURCHASE -> {
                        navController.navigate(R.id.navigation_shop)
                    }
                }
            }
    }

    fun setActionBarTitle(title: String?) {
        supportActionBar?.title = title
    }

    fun updateBottomNavCartButtonText(size: Int) {
        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_nav)
        val item: MenuItem = bottomNavigation.menu.findItem(R.id.navigation_cart)
        when(size) {
            0 -> item.title = getString(R.string.nav_cart)
            else -> item.title = getString(R.string.nav_cart) + " (${size})"
        }
    }
}