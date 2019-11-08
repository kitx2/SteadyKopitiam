package com.example.steadykopitiam.ui.profile

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.steadykopitiam.R
import com.example.steadykopitiam.ui.about.AboutActivity
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.purchases.PurchasesActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.drawerLayout
import kotlinx.android.synthetic.main.activity_home.navigationView
import kotlinx.android.synthetic.main.activity_home.toolbar
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import com.github.florent37.expansionpanel.ExpansionLayout


class ProfileActivity : AppCompatActivity() {
    //TODO: Reference username from Database
    val username : String = "Test Username"

    var navigationPosition: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.steadykopitiam.R.layout.activity_profile)
        setTitle("Profile")
        initView()

        val basicInfoCard : CardView = findViewById(R.id.basicInfoCard)

        basicInfoCard.setOnClickListener(){
            val myIntent = Intent(this, EditProfileActivity::class.java)
            startActivity(myIntent)
            this.overridePendingTransition(0, 0)
        }

        val expansionLayout = findViewById<ExpansionLayout>(R.id.expansionLayout);
        expansionLayout.addListener(ExpansionLayout.Listener { expansionLayout, expanded ->
             fun onExpansionChanged(expansionLayout: ExpansionLayout, boolean: Boolean) {

            }
        })


    }

    //Side Navbar
    private fun initView() {
        val toolbar: Toolbar = findViewById(com.example.steadykopitiam.R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(com.example.steadykopitiam.R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(com.example.steadykopitiam.R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = com.example.steadykopitiam.R.id.nav_profile
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "Profile"

        navigationView.setNavigationItemSelectedListener  { menuItem: MenuItem ->
            when (menuItem.itemId) {
                com.example.steadykopitiam.R.id.nav_home -> {
                    toolbar.title = "Home"
                    navigationPosition = com.example.steadykopitiam.R.id.nav_home
                    val myIntent = Intent(this, HomeActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                com.example.steadykopitiam.R.id.nav_purchases -> {
                    toolbar.title = "Purchases"
                    navigationPosition = com.example.steadykopitiam.R.id.nav_purchases
                    val myIntent = Intent(this, PurchasesActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                com.example.steadykopitiam.R.id.nav_profile -> {
                    toolbar.title = "Profile"
                    navigationPosition = com.example.steadykopitiam.R.id.nav_profile
                    val myIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                com.example.steadykopitiam.R.id.nav_wallet -> {
                    toolbar.title = "Wallet"
                    navigationPosition = com.example.steadykopitiam.R.id.nav_wallet
                    val myIntent = Intent(this, WalletActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                com.example.steadykopitiam.R.id.nav_about -> {
                    toolbar.title = "About"
                    navigationPosition = com.example.steadykopitiam.R.id.nav_about
                    val myIntent = Intent(this, AboutActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
            }
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()
            true
        }

        //Change navigation header information
        changeNavigationHeaderInfo()

//        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
//            override fun onDrawerStateChanged(p0: Int) {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDrawerSlide(p0: View, p1: Float) {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDrawerClosed(p0: View) {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//
//            override fun onDrawerOpened(p0: View) {
////                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//            }
//        })
    }

    private fun changeNavigationHeaderInfo() {
        val headerView = navigationView.getHeaderView(0)
        headerView.username.text = username
    }

    private fun setUpDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, com.example.steadykopitiam.R.string.navigation_drawer_open, com.example.steadykopitiam.R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val toolbar: Toolbar = findViewById(com.example.steadykopitiam.R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(com.example.steadykopitiam.R.id.drawerLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (navigationPosition == com.example.steadykopitiam.R.id.nav_profile) {
            finish()
        } else {
            //Navigate to Profile
            navigationPosition = com.example.steadykopitiam.R.id.nav_profile
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "Profile"
        }
    }
}

