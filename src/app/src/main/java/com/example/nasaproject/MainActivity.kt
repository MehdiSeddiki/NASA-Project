package com.example.nasaproject

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {
    private val apodViewModel by viewModels<ApodViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.apodFragment, R.id.mrpFragment, R.id.eonetFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        observePicList()
    }

    fun observePicList() {
        apodViewModel.picList.observe(this, Observer { newValue -> println(newValue) })
    }

    class ApodViewModel(application: Application) : AndroidViewModel(application) {
        val picList = MutableLiveData<List<ApodObject>>()

        fun setPicList(value: List<ApodObject>) {
            picList.value = value
        }
    }
}