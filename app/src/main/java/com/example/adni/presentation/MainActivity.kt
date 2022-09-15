package com.example.adni.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.adni.R
import com.example.adni.presentation.company.list.SearchVisibilityCallBack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = supportFragmentManager.findFragmentById(R.id.fragment)!!.findNavController()

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.label == "Entreprises") {
            val fragmentSearchCallback = supportFragmentManager.findFragmentById(R.id.fragment)?.childFragmentManager
                ?.fragments?.get(0) as SearchVisibilityCallBack
            fragmentSearchCallback.searchViewVisible()?.let {
                if (it)
                    fragmentSearchCallback.closeSearchView()
            }
        }
        else super.onBackPressed()
    }
}