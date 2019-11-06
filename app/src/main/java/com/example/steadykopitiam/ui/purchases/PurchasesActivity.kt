package com.example.steadykopitiam.ui.purchases

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steadykopitiam.*
import com.example.steadykopitiam.ui.about.AboutActivity
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.profile.ProfileActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.text.SimpleDateFormat
import java.util.*

class PurchasesActivity : AppCompatActivity() {
    //TODO: Reference username from Database
    val username : String = "Test Username"

    var navigationPosition: Int = 1

    //Orderlist
    private var orderRecycleView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelOrderVertical>? = null
    private var adapter: AdapterOrderViewVertical? = null

    val sdf = SimpleDateFormat("dd/MM/yyyy, HH:mm")
    val currentDate = sdf.format(Date())
    private var foodName : String = ""
    private var foodPrice : Double = 0.0
    private var coinEarned : Double = 0.0

    //TODO: Update foodList
    private val myOrderDateList = arrayOf(currentDate.toString())
    private val myOrderNameList = arrayListOf<String>()
    private val myOrderPointList = DoubleArray(size=1)
    var double : Double = 4.00
    var pricetag : String = "S$".plus(String.format("%.2f", double))
    private var myOrderPriceList : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchases)
        setTitle("Purchases")
        initView()

        foodName = intent.getStringExtra("foodName")
        foodPrice = intent.getDoubleExtra("foodPrice",0.0)
        coinEarned = intent.getDoubleExtra("coinEarced",0.0)
        //Order list


        myOrderNameList.add(foodName)
        myOrderPointList[0] = coinEarned
        myOrderPriceList = foodPrice
        orderRecycleView = findViewById(R.id.orderRecycleView)

        imageModelArrayList = populateList()
        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = AdapterOrderViewVertical(applicationContext, imageModelArrayList!!)
        orderRecycleView!!.adapter = adapter
        orderRecycleView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

    }

    private fun populateList(): ArrayList<ModelOrderVertical> {

        val list = ArrayList<ModelOrderVertical>()

            val orderModel = ModelOrderVertical()
            orderModel.setNames(myOrderNameList[0])
            orderModel.setDates(myOrderDateList[0])
            orderModel.setPoints(myOrderPointList[0].toString())
            orderModel.setPrices(myOrderPriceList.toString())
            list.add(orderModel)


        return list
    }

    //Side Navbar
    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = R.id.nav_purchases
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "Purchases"

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

        if (navigationPosition == R.id.nav_purchases) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = R.id.nav_purchases
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "Purchases"
        }
    }
}
