package com.example.steadykopitiam.ui.home

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_home.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steadykopitiam.*
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import java.util.ArrayList

class HomeActivity : AppCompatActivity() {

    var navigationPosition: Int = 0

    //Steady picks
    private var steadyPicksRecyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelFoodHorizontal>? = null
    private var adapter: AdapterFoodViewHorizontal? = null
    //TODO: Update foodList
    private val myImageList = intArrayOf(R.drawable.chicken_rice, R.drawable.char_siew_rice, R.drawable.fishball_noodle_dry, R.drawable.minced_pork_noodle, R.drawable.duck_rice, R.drawable.kway_chap, R.drawable.lor_mee, R.drawable.fried_rice, R.drawable.fried_carrot_cake)
    private val myImageNameList = arrayOf("Chicken rice", "Char siew rice", "Fishball noodle(Dry)", "Minced pork noodle", "Duck rice", "Kway chap", "Lor mee", "Fried rice", "Fried carrot cake")
    private val myImageDescriptionList = arrayOf(" Chicken, roasted, with skin, served with rice and chilli sauce.", "Pork barbequed in sweet sauce, served with rice and cucumber.", "Yellow noodles with fish ball and chye sim, served with chili sauce.", "Minced pork noodle", "Duck rice", "Kway chap", "Lor mee", "Fried rice", "Fried carrot cake")

    //All Stall
    private var stallRecyclerView: RecyclerView? = null
    private var stallImageModelArrayList: ArrayList<ModelStallVertical>? = null
    private var stallAdapter: AdapterStallViewVertical? = null
    //TODO: Update StallList
    private val stallMyImageList = intArrayOf(R.drawable.chicken_rice, R.drawable.char_siew_rice, R.drawable.fishball_noodle_dry, R.drawable.minced_pork_noodle, R.drawable.duck_rice, R.drawable.kway_chap, R.drawable.lor_mee, R.drawable.fried_rice, R.drawable.fried_carrot_cake)
    private val stallMyImageNameList = arrayOf("Hainanese Chicken Rice", "Roasted Delights", "Fishball Noodle Stall", "Blk 85 Minced Pork Noodle", "Wen Ji Duck Rice", "Quan Lai Kway Chap", "Ma Bo Lor Mee", "Ah Meng Fried Rice", "Song Zhou Fried Carrot Cake")
    private val stallMyImageDescriptionList = arrayOf("Singapore, Rice", "Singapore, rice", "Singapore, Noodle", "Singapore, Noodle", "Singapore, Rice", "Singapore, Noodle", "Singapore, Noodle", "Singapore, Rice", "Singapore, Dim Sum")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initView()
        setTitle("Home")

        //Steady Picks
        steadyPicksRecyclerView = findViewById(R.id.SteadyPicksRecycleViewer)

        imageModelArrayList = populateList()
        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = AdapterFoodViewHorizontal(applicationContext, imageModelArrayList!!)
        steadyPicksRecyclerView!!.adapter = adapter
        steadyPicksRecyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        steadyPicksRecyclerView!!.addOnItemTouchListener(
            HomeActivity.RecyclerTouchListener(
                applicationContext,
                steadyPicksRecyclerView!!,
                object : HomeActivity.ClickListener {

                    override fun onClick(view: View, position: Int) {
                        //TODO: Start new Activity here
                        Toast.makeText(
                            applicationContext,
                            imageModelArrayList!![position].getNames(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )


        //Stalls
        stallRecyclerView = findViewById(R.id.StallsRecycleViewer)

        stallImageModelArrayList = populateStallList()
        Log.d("hjhjh", stallImageModelArrayList!!.size.toString() + "")
        stallAdapter = AdapterStallViewVertical(applicationContext, stallImageModelArrayList!!)
        stallRecyclerView!!.adapter = stallAdapter
        stallRecyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

        stallRecyclerView!!.addOnItemTouchListener(
            HomeActivity.RecyclerTouchListener(
                applicationContext,
                stallRecyclerView!!,
                object : HomeActivity.ClickListener {

                    override fun onClick(view: View, position: Int) {
                        //TODO: Start new Activity here
                        Toast.makeText(
                            applicationContext,
                            stallImageModelArrayList!![position].getNames(),
                            Toast.LENGTH_SHORT
                        ).show()

                        val myIntent = Intent(applicationContext, StallActivity::class.java)
                        startActivity(myIntent)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

    }

    private fun populateList(): ArrayList<ModelFoodHorizontal> {

        val list = ArrayList<ModelFoodHorizontal>()
        for (i in myImageList.indices) {
            val imageModel = ModelFoodHorizontal()
            imageModel.setNames(myImageNameList[i])
            imageModel.setImage_drawables(myImageList[i])
            imageModel.setDescriptions(myImageDescriptionList[i])
            list.add(imageModel)
        }

        return list
    }

    private fun populateStallList(): ArrayList<ModelStallVertical> {

        val list = ArrayList<ModelStallVertical>()
        for (i in stallMyImageList.indices) {
            val imageModel = ModelStallVertical()
            imageModel.setNames(stallMyImageNameList[i])
            imageModel.setImage_drawables(stallMyImageList[i])
            imageModel.setDescriptions((stallMyImageDescriptionList[i]))
            list.add(imageModel)
        }

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: HomeActivity.ClickListener?) : RecyclerView.OnItemTouchListener {

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
