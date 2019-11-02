package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import java.util.ArrayList

class StallActivity : AppCompatActivity() {

    var navigationPosition: Int = 0

    //Food item in a stall
    private var foodRecyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelFoodVertical>? = null
    private var adapter: AdapterFoodViewVertical? = null

    //TODO: Update foodList
    private val myImageList = intArrayOf(R.drawable.chicken_rice, R.drawable.char_siew_rice, R.drawable.fishball_noodle_dry, R.drawable.minced_pork_noodle, R.drawable.duck_rice, R.drawable.kway_chap, R.drawable.lor_mee, R.drawable.fried_rice, R.drawable.fried_carrot_cake)
    private val myImageNameList = arrayOf("Chicken rice", "Char siew rice", "Fishball noodle(Dry)", "Minced pork noodle", "Duck rice", "Kway chap", "Lor mee", "Fried rice", "Fried carrot cake")
    private val myImageDescriptionList = arrayOf(" Chicken, roasted, with skin, served with rice and chilli sauce.", "Pork barbequed in sweet sauce, served with rice and cucumber.", "Yellow noodles with fish ball and chye sim, served with chili sauce.", "Minced pork noodle", "Duck rice", "Kway chap", "Lor mee", "Fried rice", "Fried carrot cake")
    private val myImagePriceList = arrayOf(" $3.00", "$3.00", "$4.00", "$4.00", "$4.00", "$4.00", "$4.00","$4.00", "$3.50", "$4.00", "$4.00")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stall)
        initView()

        //TODO: update stall name
        val stallName = "Stall name"
        setTitle(stallName)

        //Steady Picks
        foodRecyclerView = findViewById(R.id.foodRecyclerView)

        imageModelArrayList = populateList()
        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = AdapterFoodViewVertical(applicationContext, imageModelArrayList!!)
        foodRecyclerView!!.adapter = adapter
        foodRecyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        foodRecyclerView!!.addOnItemTouchListener(
            StallActivity.RecyclerTouchListener(
                applicationContext,
                foodRecyclerView!!,
                object : StallActivity.ClickListener {

                    override fun onClick(view: View, position: Int) {
                        Toast.makeText(
                            applicationContext,
                            imageModelArrayList!![position].getNames(),
                            Toast.LENGTH_SHORT
                        ).show()

                        //Start new Activity
                        //TODO: Pass stall name to QR Activity
                        val myIntent = Intent(applicationContext, QRActivity::class.java)
                        startActivity(myIntent)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

    }
    private fun populateList(): ArrayList<ModelFoodVertical> {

        val list = ArrayList<ModelFoodVertical>()
        for (i in myImageList.indices) {
            val imageModel = ModelFoodVertical()
            imageModel.setNames(myImageNameList[i])
            imageModel.setImage_drawables(myImageList[i])
            imageModel.setDescriptions(myImageDescriptionList[i])
            imageModel.setPrices(myImagePriceList[i])
            list.add(imageModel)
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: StallActivity.ClickListener?) : RecyclerView.OnItemTouchListener {

        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {

            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

        }
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = R.id.nav_home
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "Home"

        navigationView.setNavigationItemSelectedListener  { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    toolbar.title = "Home"
                    navigationPosition = R.id.nav_home
                    val myIntent = Intent(this, HomeActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }
                R.id.nav_purchases -> {
                    toolbar.title = "Purchases"
                    navigationPosition = R.id.nav_purchases
                }
                R.id.nav_profile -> {
                    toolbar.title = "Profile"
                    navigationPosition = R.id.nav_profile
                }
                R.id.nav_wallet -> {
                    toolbar.title = "Wallet"
                    navigationPosition = R.id.nav_wallet
                    val myIntent = Intent(this, WalletActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }
                R.id.nav_about -> {
                    toolbar.title = "About"
                    navigationPosition = R.id.nav_about
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
//        val headerView = navigationView.getHeaderView(0)
//        headerView.textEmail.text = "lokeshdesai@android4dev.com"
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

        if (navigationPosition == R.id.nav_home) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = R.id.nav_home
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "Home"
        }
    }
}
