package com.example.steadykopitiam.ui.wallet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.steadykopitiam.DBHelper
import com.example.steadykopitiam.R
import com.example.steadykopitiam.UserRecord
import com.example.steadykopitiam.ui.about.AboutActivity
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.profile.ProfileActivity
import com.example.steadykopitiam.ui.purchases.PurchasesActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.nav_header_main.view.*

class WalletActivity : AppCompatActivity() {
    //TODO: Reference username from Database
    val username : String = "Test Username"
    var navigationPosition: Int = 3
    lateinit var kopitiamDBHelper: DBHelper

    //User details
    var user = ArrayList<UserRecord>()
    private var userpassword : String? = ""
    private var useremail  : String? = ""
    var accountBalance: Double = 0.0
    var accountPoints: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        initView()
        setTitle("Wallet")

        var topUpValue: Int = 0
        val btnTen: Button = findViewById(R.id.btnTen)
        val btnTwenty: Button = findViewById(R.id.btnTwenty)
        val btnFifty: Button = findViewById(R.id.btnFifty)
        val topUpAmount: TextView = findViewById(R.id.topUpAmount)
        val coinBalance: TextView = findViewById(R.id.coinBalance)
        val currentBalance: TextView = findViewById(R.id.currentBalance)

        /* TODO
         #1 - retrieve steady coins from user table
         #2 - retrieve current balance from user table
         */
        //coinBalance.text =
        //currentBalance.text =
        //var currentBal: Double = currentBalance.text.substring(1,currentBalance.text.length).toDouble()

        btnTen.setOnClickListener {
            topUpValue = 10
            topUpAmount.text = "$" + topUpValue
        }
        btnTwenty.setOnClickListener {
            topUpValue = 20
            topUpAmount.text = "$" + topUpValue
        }
        btnFifty.setOnClickListener {
            topUpValue = 50
            topUpAmount.text = "$" + topUpValue
        }
        val btnTopUp: Button = findViewById(R.id.btnTopUp)
        btnTopUp.setOnClickListener { view ->
            if(topUpAmount.text != "Top up amount") {
                Toast.makeText(
                    applicationContext,
                    "Successfully top up " + topUpValue + " into your wallet.",
                    Toast.LENGTH_SHORT
                ).show()
                topUpAmount.text = "Top up amount"
                topUpWallet(topUpValue)
                fetchUser()
            } else {
                Toast.makeText(
                    applicationContext,
                    "Select a top up value",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        fetchUser()
    }

    fun fetchUser() {
        val coinBalance : TextView = findViewById(R.id.coinBalance)
        val currentBalance : TextView = findViewById(R.id.currentBalance)


        kopitiamDBHelper = DBHelper(this)

        //TODO: SetText and Set Index position of spinner
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        userpassword = preferences.getString("userPassword", "")
        useremail = preferences.getString("userEmail", "")
        user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)

        if (!user.isEmpty()) {
            accountBalance = user.get(0).accountBalance
            accountPoints = user.get(0).accountPoints

            coinBalance.text = accountPoints.toString()
            currentBalance.text = String.format("$%.2f",accountBalance)
        }



    }

    fun topUpWallet(TopUpValue:Int) {
        var finalAccountBalance = accountBalance + TopUpValue
        //TODO: SetText and Set Index position of spinner
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        userpassword = preferences.getString("userPassword", "")
        useremail = preferences.getString("userEmail", "")
        user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)

        if(!user.isNullOrEmpty()) {
            var result = kopitiamDBHelper.updateUser(
                UserRecord(user.get(0).username,user.get(0).gender,user.get(0).height,user.get(0).weight,
                    user.get(0).bmi,user.get(0).age,user.get(0).email,finalAccountBalance,user.get(0).accountPoints,user.get(0).user_carbs,user.get(0).user_calories,
                    user.get(0).user_fat,user.get(0).user_fibre,user.get(0).user_minerals,user.get(0).user_vitamins,user.get(0).user_dailyActivies,user.get(0).user_protein,user.get(0).user_password,user.get(0).phoneNumber)
            )
        }

    }


    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load current activity
        navigationPosition = R.id.nav_wallet
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "Wallet"

        navigationView.setNavigationItemSelectedListener  { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    toolbar.title = "Home"
                    navigationPosition = R.id.nav_home
                    val myIntent = Intent(this, HomeActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_purchases -> {
                    toolbar.title = "Purchases"
                    navigationPosition = R.id.nav_purchases
                    val myIntent = Intent(this, PurchasesActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_profile -> {
                    toolbar.title = "Profile"
                    navigationPosition = R.id.nav_profile
                    val myIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_wallet -> {
                    toolbar.title = "Wallet"
                    navigationPosition = R.id.nav_wallet
                    val myIntent = Intent(this, WalletActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_about -> {
                    toolbar.title = "About"
                    navigationPosition = R.id.nav_about
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
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (navigationPosition == R.id.nav_wallet) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = R.id.nav_wallet
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "Wallet"
        }
    }
}
