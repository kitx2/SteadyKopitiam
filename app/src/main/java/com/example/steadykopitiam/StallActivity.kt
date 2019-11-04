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
import androidx.core.content.ContextCompat.startActivity
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
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.util.ArrayList

class StallActivity : AppCompatActivity() {

    var navigationPosition: Int = 0

    //Food item in a stall
    private var foodRecyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelFoodVertical>? = null
    private var adapter: AdapterFoodViewVertical? = null

    //TODO: Update foodList
    private val myImageList = mutableListOf<Int>()
    private val myImageNameList = arrayListOf<String>()
    private val myImageDescriptionList = arrayListOf<String>()
    private val myImagePriceList =  arrayListOf<String>()


    //TODO: update stall name
    private var stallName : String = ""


    //TODO: Update StallList

    private var stallMyImageList : Int = 0
    private var stallMyImageDescriptionList : String = ""
//    private val stallMyImageDescriptionList : String = intent.getStringExtra("stallDescription")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stall)
        initView()
        stallName = intent.getStringExtra("stallName")
        println("Stall name " + stallName)
        setTitle(stallName)
        val stallImageId : Int = intent.getIntExtra("stallImageid",0)
        stallMyImageDescriptionList = intent.getStringExtra("stallDescription")
        println("stall image ID "+  stallImageId)
        readFood()

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
                        myIntent.putExtra("foodName",imageModelArrayList!![position].getNames())
                        myIntent.putExtra("stallName",stallName)
                        myIntent.putExtra("foodImage,",imageModelArrayList!![position].getImage_drawables())
                        myIntent.putExtra("foodPrice",imageModelArrayList!![position].getPrices())
                        startActivity(myIntent)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

    }
    // read the all the food from the stall selected
    fun readFood(){

        var json : String? = null
        try{

            val inputStream : InputStream = applicationContext.assets.open(stallName)
            json = inputStream.bufferedReader().readText()
            var jsonArray = JSONArray(json)

            for(i in 0..jsonArray.length()-1){
                var jsonOjb = jsonArray.getJSONObject(i)
                println("Food name is "+jsonOjb.getString("foodName"))

                var tempRec : String
                // get food image
                tempRec= (jsonOjb.getString("foodResourceId"))
                var final = resources.getIdentifier(tempRec,"drawable",this.packageName)
                myImageList.add(final)

//                // get stall image
//                var tempStall : String
//                tempStall = jsonOjb.getString("stallImageId")
//                var stallImg = resources.getIdentifier(tempStall,"drawable",this.packageName)
//                stallMyImageList = stallImg

                // update food infor
                myImagePriceList.add(jsonOjb.getString("foodBasePrice"))
                myImageNameList.add(jsonOjb.getString("foodName"))
                myImageDescriptionList.add(jsonOjb.getString("foodDescription"))

            }
        }catch(e: IOException){
        }
    }

    private fun readStallJson(){
        var json : String? = null
        try{

        }catch (e: IOException){

        }
    }
    private fun populateList(): ArrayList<ModelFoodVertical> {

        val list = ArrayList<ModelFoodVertical>()
        println("Food size +***"+ myImageList.size)
        println("iamge name size  " + myImageNameList.size)
        for (i in 0..myImageList.size-1) {
            val imageModel = ModelFoodVertical()
            imageModel.setNames(myImageNameList[i])
            imageModel.setImage_drawables(myImageList[i])
            imageModel.setDescriptions(myImageDescriptionList[i])
            imageModel.setPrices(myImagePriceList[i])
            list.add(imageModel)
        }

        return list
    }


    private fun populateStallList(): ArrayList<ModelStallVertical> {

        val list = ArrayList<ModelStallVertical>()

            val imageModel = ModelStallVertical()
            imageModel.setNames("")
            imageModel.setImage_drawables(stallMyImageList)
            imageModel.setDescriptions((stallMyImageDescriptionList))
            list.add(imageModel)


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
