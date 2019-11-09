package com.example.steadykopitiam.ui.profile

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.steadykopitiam.*
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
import kotlinx.android.synthetic.main.expansion_panel_macro_panel.*
import kotlinx.android.synthetic.main.expansion_panel_other_panel.*
import kotlin.math.roundToInt


class ProfileActivity : AppCompatActivity() {
    //TODO: Reference username from Database
    lateinit var kopitiamDBHelper: DBHelper
    var username : String? = ""

    private var userpassword : String? = ""
    private var useremail : String? = ""
    // variable to track data
    private var user = ArrayList<UserRecord>()
    private var carbs : Int = 0
    private var protein : Int = 0
    private var fat : Int = 0
    private var fibre : Int = 0
    private var vitamins : Double = 0.0
    private var minerals : Double = 0.0
    private var calories : Int = 0
    private var bmi : Double = 0.0
    private var orderList = ArrayList<OrderSummaryRecord>()

    private var carbsIntake : Int = 0
    private var proteinIntakeFromOrdSum : Int = 0
    private var fatIntakeFromOrdSum : Int = 0
    private var fibreIntakeFromOrdSum : Int = 0
    private var vitaminsIntakeFromOrdSum : Double = 0.0
    private var mineralsIntakeFromOrdSum : Double = 0.0
    private var caloriesIntakeFromOrdSum : Int = 0
    private var bmiIntake : Double = 0.0


    var navigationPosition: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.steadykopitiam.R.layout.activity_profile)
        setTitle("Profile")


        kopitiamDBHelper = DBHelper(this)

        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        userpassword = preferences.getString("userPassword", "")
        useremail = preferences.getString("userEmail", "")
        username = preferences.getString("username","")
        initView()
        this.nameLabel.text = username

        retrieveUserNutrition(useremail!!,userpassword!!)
        this.calorieMax.text = calories.toString()+"g"
        this.carbMax.text = carbs.toString()+"g"
        this.proteinMax.text = protein.toString()+"g"
        this.fatMax.text = fat.toString()+"g"
        this.fiberMax.text = fibre.toString()+"g"
        this.mineralMax.text = String.format("%.2f",minerals)+"g"
        this.vitaminMax.text = String.format("%.2f",vitamins)+"g"

        retrieveTotalNutritionIntake()
        this.calorieIntake.text = caloriesIntakeFromOrdSum.toString()+"g"
        this.carbIntake.text = carbsIntake.toString()+"g"
        this.proteinIntake.text = proteinIntakeFromOrdSum.toString()+"g"
        this.fatIntake.text = fatIntakeFromOrdSum.toString()+"g"
        this.fiberIntake.text = fibreIntakeFromOrdSum.toString()+"g"
        this.mineralIntake.text = String.format("%.2f",mineralsIntakeFromOrdSum)+"g"
        this.vitaminIntake.text = String.format("%.2f",vitaminsIntakeFromOrdSum)+"g"

        this.caloriePercent.text = (((caloriesIntakeFromOrdSum.toDouble() /calories.toDouble())*100).roundToInt()).toString() +"%"
        this.carbPercent.text = (((carbsIntake.toDouble() / carbs.toDouble())*100).roundToInt()).toString()+"%"
        this.proteinPercent.text = (((proteinIntakeFromOrdSum.toDouble() / protein.toDouble())*100).roundToInt()).toString()+"%"
        this.fatPercent.text = (((fatIntakeFromOrdSum.toDouble() / fat.toDouble())*100).roundToInt()).toString()+"%"
        this.fiberPercent.text = (((fibreIntakeFromOrdSum.toDouble() / fibre.toDouble())*100).roundToInt()).toString()+"%"
        this.mineralPercent.text = (((mineralsIntakeFromOrdSum / minerals)*100).roundToInt()).toString()+"%"
        this.vitaminPercent.text = (((vitaminsIntakeFromOrdSum / vitamins)*100).roundToInt()).toString()+"%"

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


        val btnLogOut :Button = findViewById<Button>(R.id.btnLogout)
        btnLogOut.setOnClickListener{
            val logoutPrefEditor = preferences.edit()
            logoutPrefEditor.putString("userPassword","")
            logoutPrefEditor.putString("userEmail","")
            logoutPrefEditor.putString("username","")
            logoutPrefEditor.commit()

            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }
    }


    private fun retrieveUserNutrition(useremail:String ,userpassword:String ){
        user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)
        username = user.get(0).username
        carbs = user.get(0).user_carbs
        protein = user.get(0).user_protein
        fat = user.get(0).user_fat
        fibre = user.get(0).user_fibre
        vitamins = user.get(0).user_vitamins
        calories = user.get(0).user_calories
        minerals = user.get(0).user_minerals
        bmi = user.get(0).bmi
    }

    private fun retrieveTotalNutritionIntake(){
        orderList = kopitiamDBHelper.retrieveAllOrderSummary()
        for(i in 0..orderList.size-1){
            println("Foodname !!!!!"+ orderList[i].orderSummaryFoodName)
            carbsIntake = carbsIntake + orderList[i].orderSummaryFoodCarbs
            proteinIntakeFromOrdSum = proteinIntakeFromOrdSum + orderList[i].orderSummaryProtein
            fatIntakeFromOrdSum = fatIntakeFromOrdSum + orderList[i].orderSummaryFat
            fibreIntakeFromOrdSum = fibreIntakeFromOrdSum + orderList[i].orderSummaryFibre
            vitaminsIntakeFromOrdSum = vitaminsIntakeFromOrdSum + orderList[i].orderSummaryVitamins
            caloriesIntakeFromOrdSum = caloriesIntakeFromOrdSum + orderList[i].orderSummaryCalories
            mineralsIntakeFromOrdSum = mineralsIntakeFromOrdSum + orderList[i].orderSummaryMinerals
        }
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

