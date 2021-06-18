package com.oblessing.mediapark.core

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.oblessing.mediapark.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupBottomBar()
    }

    private fun setupBottomBar() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }
}