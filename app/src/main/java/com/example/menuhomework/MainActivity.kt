package com.example.menuhomework

import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.menuhomework.retrofit.model.WeatherRequest
import com.example.menuhomework.databinding.ActivityMainBinding
import com.example.menuhomework.interfaces.FragmentCityResult
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso

private const val ARG_PARAM1 = "param1"


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    FragmentCityResult {
    private var binding: ActivityMainBinding? = null
    private var data: MutableList<WeatherRequest> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val toolbar = initToolbar()
        initDrawer(toolbar)

//        val uri = Uri.parse("http://openweathermap.org/img/w/04d.png")
//
//        Picasso.get()
//            .load(uri)
//            .into(binding?.appBarMain?.imageView)

        if (savedInstanceState == null)
            replaceFragment(CityFragment.newInstance(this))
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null
    }

    private fun initDrawer(toolbar: Toolbar) {
        val drawer = binding?.drawerLayout
        val navigationView = binding?.navView
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer?.addDrawerListener(toggle)
        toggle.syncState()
        navigationView?.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.nav_home) {
            replaceFragment(CityFragment.newInstance(this))
        } else if (id == R.id.nav_search) {
            val fragment = SearchFragment()
            fragment.receiveData(data)
            data = ArrayList()
            replaceFragment(fragment)
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        }
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit()
    }

    private fun initToolbar(): Toolbar {
        val toolbar = binding?.appBarMain?.toolbar
        setSupportActionBar(toolbar)
        return toolbar!!
    }

    override fun onBackPressed() {
        val drawer = binding?.drawerLayout as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onFragmentResult(item: WeatherRequest) {
        data.add(item)
    }
}