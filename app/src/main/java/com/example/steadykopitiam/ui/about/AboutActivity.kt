package com.example.steadykopitiam.ui.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.steadykopitiam.R
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.profile.ProfileActivity
import com.example.steadykopitiam.ui.purchases.PurchasesActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class AboutActivity : AppCompatActivity() {
    //TODO: Reference username from Database
    private var username : String? = ""

    var navigationPosition: Int = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.steadykopitiam.R.layout.activity_about)
        setTitle("About")
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        username = preferences.getString("username","")
        initView()

        val versionLabel = findViewById<TextView>(com.example.steadykopitiam.R.id.versionLabel)
        versionLabel.text = getApplicationVersion()

        val sendEmailBtn = findViewById<Button>(R.id.btnFeedback)
        //button click to get input and call sendEmail method
        sendEmailBtn.setOnClickListener {
            //get input from EditTexts and save in variables
            val recipient = "e0119427@u.nus.edu; e0052936@u.nus.edu;"
            val subject = "Feedback on Steady Kopitiam"
            val message = ""

            //method call for email intent with these inputs as parameters
            sendEmail(recipient, subject, message)
        }

    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        /*ACTION_SEND action to launch an email client installed on your Android device.*/
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

    //Retrieve App version
    private fun getApplicationVersion(): String {
        var versionStr: String = ""
        try {
            val pInfo = this.getPackageManager().getPackageInfo(packageName, 0)
            versionStr = "Version " + pInfo.versionName
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return versionStr
    }

    //Side Navbar
    private fun initView() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(com.example.steadykopitiam.R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(com.example.steadykopitiam.R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(com.example.steadykopitiam.R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = com.example.steadykopitiam.R.id.nav_about
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "About"

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
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(com.example.steadykopitiam.R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(com.example.steadykopitiam.R.id.drawerLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (navigationPosition == com.example.steadykopitiam.R.id.nav_about) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = com.example.steadykopitiam.R.id.nav_about
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "About"
        }
    }
}

