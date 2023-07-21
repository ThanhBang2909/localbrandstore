package com.example.localbrandstore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.localbrandstore.activity.HomeFragment
import com.example.localbrandstore.activity.NotificationFragment
import com.example.localbrandstore.activity.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView ?= null
    private var fragmentTransaction:FragmentTransaction ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        eventClick()
        openFragment(HomeFragment())
    }

    private fun eventClick(){
        bottomNavigationView?.setOnItemSelectedListener { item ->
            val iD = item.itemId
            val fragment = when (iD) {
                R.id.mnuHome -> HomeFragment()
                R.id.mnuNotifications -> NotificationFragment()
                R.id.mnuProfile -> ProfileFragment()
                else -> null
            }
            fragment?.let { openFragment(fragment) }
            true
        }

    }

    private fun openFragment(f: Fragment){
        fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction!!.replace(R.id.bottom_nav_framelayout, f)
        fragmentTransaction!!.addToBackStack(null)
        fragmentTransaction!!.commit()
    }

    private fun initView(){
        bottomNavigationView = findViewById(R.id.bottomnavigation);
    }
}