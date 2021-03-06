package com.example.schedule.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.schedule.R
import com.example.schedule.ui.schedule.editDialog.IFragmentMovement
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), IFragmentMovement {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        /* val appBarConfiguration = AppBarConfiguration.Builder(
                 R.id.navigation_schedule, R.id.navigation_tasks, R.id.navigation_settings)
                 .build()*/
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fm = supportFragmentManager
                if (fm.backStackEntryCount > 0) fm.popBackStack() else finish()
            }
        })
    }

    override fun onMove(from: Fragment, to: Fragment, intention: String, name: String) {
        from.parentFragmentManager
                .beginTransaction()
                .addToBackStack(name)
                .hide(from)
                .replace(R.id.nav_host_fragment, to)
                .commit()
    }
}
