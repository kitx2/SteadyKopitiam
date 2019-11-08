package com.example.steadykopitiam.ui.home

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import kotlinx.android.synthetic.main.activity_home.*
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steadykopitiam.*
import com.example.steadykopitiam.ui.about.AboutActivity
import com.example.steadykopitiam.ui.profile.ProfileActivity
import com.example.steadykopitiam.ui.purchases.PurchasesActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    //TODO: Reference username from Database
    var username : String? = ""
    lateinit var kopitiamDBHelper: DBHelper
    lateinit var sharedPreForRecc : SharedPreferences
    var navigationPosition: Int = 0
    private var stallName : String = ""
    private var stallDescription : String = ""
    private var stallImageId : Int = 0

    //Steady picks
    private var steadyPicksRecyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelFoodHorizontal>? = null
    private var adapter: AdapterFoodViewHorizontal? = null
    private var foodNameInRecc : String = ""
    private var stallNameInRecc : String = ""
    private var foodPrice : String = ""


    //TODO: Update foodList
    private var myImageList = IntArray(size = 4)
    private val myImageNameList = arrayListOf<String>()
    private val myImageDescriptionList = arrayListOf<String>()
    private val myImageFoodFocusList = arrayListOf<String>()
    private var stallnameInRecommendedList = arrayListOf<String>()
    private val myImageFoodPrice = arrayListOf<String>()

    //All Stall
    private var stallRecyclerView: RecyclerView? = null
    private var stallImageModelArrayList: ArrayList<ModelStallVertical>? = null
    private var stallAdapter: AdapterStallViewVertical? = null

    //TODO: Update StallList
    private val stallMyImageList = IntArray(size = 4)
    private val stallMyImageNameList = arrayListOf<String>()
    private val stallMyImageDescriptionList = arrayListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        kopitiamDBHelper = DBHelper(this)
        readRecommendedjson()
        readStalljson()

        setTitle("Home")


        sharedPreForRecc = getSharedPreferences("IsReccFoodSelected", Context.MODE_PRIVATE)
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        username = preferences.getString("username","")
        println("Username ++++"+username)
        initView()

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
                        foodNameInRecc = imageModelArrayList!![position].getNames()
                        stallNameInRecc = imageModelArrayList!![position].getStallName()
                        foodPrice =  imageModelArrayList!![position].getFoodPrice()
                        var sharedPreForReccEditer = sharedPreForRecc.edit()
                        sharedPreForReccEditer.putBoolean("ReccFoodIsSelected",true)
                        sharedPreForReccEditer.commit()
                        println("Food Rec in Home ----+"+sharedPreForRecc.getBoolean("ReccFoodIsSelected",false))
                        val myIntent = Intent(applicationContext,QRActivity::class.java)
                        myIntent.putExtra("foodName",foodNameInRecc)
                        myIntent.putExtra("stallName",stallNameInRecc)
                        myIntent.putExtra("foodPrice",foodPrice)
                        startActivity(myIntent)

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
                        stallName = stallImageModelArrayList!![position].getNames()
                        stallDescription = stallImageModelArrayList!![position].getDescriptions()
                        stallImageId = stallImageModelArrayList!![position].getImage_drawables()
                        val myIntent = Intent(applicationContext,StallActivity::class.java)
                        myIntent.putExtra("stallName",stallName)
                        myIntent.putExtra("stallDescription",stallDescription)
                        myIntent.putExtra("stallImageid",stallImageId)
                        Toast.makeText(
                            applicationContext,
                            stallImageModelArrayList!![position].getNames(),
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(myIntent)
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

    }


   //  --- to read data from json file --- Jy ///
    private fun readRecommendedjson(){
       // retrieve order summary and time ordered if there is not any then recommened 4 foods from different stall

       // data to pass to stall activity, stall name, stall description and image id
       var orSum = kopitiamDBHelper.retrieveAllOrderSummary()
       if(orSum.size == 0 ){
            println("order Summary is empty ^^^^^^^")
           // read recommended all stall list of food
           try{
               for(i in 0..myImageList.size-1){
                   println(" TTTTT ")
                   if(i == 0 ){
                       println(" RRRRRR ")
                       var json : String? = null
                       val inputStream : InputStream = assets.open("Wong Ah Hua")
                       json = inputStream.bufferedReader().readText()
                       var jsonArray = JSONArray(json)
                       var jsonOjb = jsonArray.getJSONObject(0)
                       var tempRec : String
                       // get food image
                       tempRec= (jsonOjb.getString("foodResourceId"))
                       var final = resources.getIdentifier(tempRec,"drawable",this.packageName)
                       myImageList[i] = final
                       // update information of stall and food
                       myImageNameList.add(jsonOjb.getString("foodName"))
                       myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                       myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                       stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                       myImageFoodPrice.add(jsonOjb.getString("foodBasePrice"))
                   }else if( i == 1 ){
                       var json : String? = null
                       val inputStream : InputStream = assets.open("Eating Healthy Kitchen")
                       json = inputStream.bufferedReader().readText()
                       var jsonArray = JSONArray(json)
                       var jsonOjb = jsonArray.getJSONObject(0)
                       var tempRec : String
                       // get food image
                       tempRec= (jsonOjb.getString("foodResourceId"))
                       var final = resources.getIdentifier(tempRec,"drawable",this.packageName)
                       myImageList[i] = final
                       // update information of stall and food
                       myImageNameList.add(jsonOjb.getString("foodName"))
                       myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                       myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                       stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                       myImageFoodPrice.add(jsonOjb.getString("foodBasePrice"))

                   }else if(i == 2 ){
                       var json : String? = null
                       val inputStream : InputStream = assets.open("Australia Signature Food")
                       json = inputStream.bufferedReader().readText()
                       var jsonArray = JSONArray(json)
                       var jsonOjb = jsonArray.getJSONObject(0)
                       var tempRec : String
                       // get food image
                       tempRec= (jsonOjb.getString("foodResourceId"))
                       var final = resources.getIdentifier(tempRec,"drawable",this.packageName)
                       myImageList[i] = final
                       // update information of stall and food
                       myImageNameList.add(jsonOjb.getString("foodName"))
                       myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                       myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                       stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                       myImageFoodPrice.add(jsonOjb.getString("foodBasePrice"))

                   }else{
                       var json : String? = null
                       val inputStream : InputStream = assets.open("Anderson Salad Kitchen")
                       json = inputStream.bufferedReader().readText()
                       var jsonArray = JSONArray(json)
                       var jsonOjb = jsonArray.getJSONObject(0)
                       var tempRec : String
                       // get food image
                       tempRec= (jsonOjb.getString("foodResourceId"))
                       var final = resources.getIdentifier(tempRec,"drawable",this.packageName)
                       myImageList[i] = final
                       // update information of stall and food
                       myImageNameList.add(jsonOjb.getString("foodName"))
                       myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                       myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                       stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                       myImageFoodPrice.add(jsonOjb.getString("foodBasePrice"))

                   }
               }
           }catch(e: IOException){
               println("Error happen here")
           }
       }else{
           var count : Int = 4
           var addCarbs : Boolean = false
           var addProtein : Boolean = false
           var addFibre : Boolean = false
           var addVitamins : Boolean = false

           for(i in 0..orSum.size-1){
               // if there is order summary check its focus and  ** dont have stall name in ordersummary  but can assgign stall name with hardcode or .
               //if focus is in it then move to next stall but what if have all focus alr then call another method to check food name
               // store focus into a arraylist then open every json file to check if focus string not the same then retrieve food item from there

               var date = orSum[i].orderSummaryTimeDate
               // convert to string to date date from order summary
               val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm:ss")
               var d = Date()
               d = simpleDateFormat.parse(date)

               val curTime = simpleDateFormat.format(Date())
               var cur = simpleDateFormat.parse(curTime)
               // means user eaten this food 1 days before
               println("FoodName "+orSum[i].orderSummaryFoodName)
               println("curr time "+ cur.time)
               println("curr time minus day  "+ (cur.time - 86400000))
               println(" order time "+ d.time)
               if((cur.time - 86400000 ) < d.time){
//                   println("FoodName "+orSum[i].orderSummaryFoodName)
//                   println("curr time "+ cur.time)
//                   println(" order time "+ d.time)
                   var focus = orSum[i].orderSummaryFocus
                   if(focus.equals("Carbs")){
                       addCarbs = true
                       count = count - 1
                   }
                   if(focus.equals("Protein")){
                       addProtein = true
                       count = count - 1
                   }
                   if (focus.equals("vitamins")){
                       addVitamins = true
                       count = count - 1
                   }
                   if (focus.equals("fibre")){
                       addFibre = true
                       count = count - 1
                   }
               }
           }
            // track recommndantion food based on food (past) order summary
           if(count > 0) {

               myImageList = IntArray(count)
               var temp: Int = 0
               if (!addCarbs && temp < count) {
                   println("Heheheheh")
                   var json: String? = null
                   val inputStream: InputStream = assets.open("Wong Ah Hua")
                   json = inputStream.bufferedReader().readText()
                   var jsonArray = JSONArray(json)
                   var jsonOjb = jsonArray.getJSONObject(0)
                   var tempRec: String
                   // get food image
                   tempRec = (jsonOjb.getString("foodResourceId"))
                   var final = resources.getIdentifier(tempRec, "drawable", this.packageName)
                   myImageList[temp] = final
                   temp = temp + 1
                   // update information of stall and food
                   myImageNameList.add(jsonOjb.getString("foodName"))
                   myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                   myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                   stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                   myImageFoodPrice.add(jsonOjb.getString("fooddeductPrice"))

               }
               if (!addFibre && temp < count) {
                   var json: String? = null
                   val inputStream: InputStream = assets.open("Australia Signature Food")
                   json = inputStream.bufferedReader().readText()
                   var jsonArray = JSONArray(json)
                   var jsonOjb = jsonArray.getJSONObject(0)
                   var tempRec: String
                   // get food image
                   tempRec = (jsonOjb.getString("foodResourceId"))
                   var final = resources.getIdentifier(tempRec, "drawable", this.packageName)
                   myImageList[temp] = final
                   // update information of stall and food
                   temp = temp + 1
                   myImageNameList.add(jsonOjb.getString("foodName"))
                   myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                   myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                   stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                   myImageFoodPrice.add(jsonOjb.getString("fooddeductPrice"))

               }

               if (!addProtein && temp < count) {
                   var json: String? = null
                   val inputStream: InputStream = assets.open("Eating Healthy Kitchen")
                   json = inputStream.bufferedReader().readText()
                   var jsonArray = JSONArray(json)
                   var jsonOjb = jsonArray.getJSONObject(0)
                   var tempRec: String
                   // get food image
                   tempRec = (jsonOjb.getString("foodResourceId"))
                   var final = resources.getIdentifier(tempRec, "drawable", this.packageName)
                   myImageList[temp] = final
                   // update information of stall and food
                   temp = temp + 1
                   myImageNameList.add(jsonOjb.getString("foodName"))
                   myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                   myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                   stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                   myImageFoodPrice.add(jsonOjb.getString("fooddeductPrice"))

               }
               if (!addVitamins && temp < count) {
                   var json: String? = null
                   val inputStream: InputStream = assets.open("Anderson Salad Kitchen")
                   json = inputStream.bufferedReader().readText()
                   var jsonArray = JSONArray(json)
                   var jsonOjb = jsonArray.getJSONObject(0)
                   var tempRec: String
                   // get food image
                   tempRec = (jsonOjb.getString("foodResourceId"))
                   var final = resources.getIdentifier(tempRec, "drawable", this.packageName)
                   myImageList[temp] = final
                   // update information of stall and food
                   temp = temp + 1
                   myImageNameList.add(jsonOjb.getString("foodName"))
                   myImageDescriptionList.add(jsonOjb.getString("foodDescription"))
                   myImageFoodFocusList.add(jsonOjb.getString("foodFocus"))
                   stallnameInRecommendedList.add(jsonOjb.getString("foodStall"))
                   myImageFoodPrice.add(jsonOjb.getString("fooddeductPrice"))
               }
           }
       }


    }
    // --- end of read recommondation --- //


    // --- read stall info   --- //
    private fun readStalljson(){
        var json : String? = null
        try{
            val inputStream : InputStream = assets.open("StallMainPageInfo")
            json = inputStream.bufferedReader().readText()
            var jsonArray = JSONArray(json)
            for(i in 0..jsonArray.length()-1){
                var jsonOjb = jsonArray.getJSONObject(i)
                // get stall image
                var tempStall : String
                tempStall = jsonOjb.getString("stallImage")
                var stallImg = resources.getIdentifier(tempStall,"drawable",this.packageName)
                stallMyImageList[i] = stallImg
                stallMyImageNameList.add(jsonOjb.getString("stallName"))
                stallMyImageDescriptionList.add(jsonOjb.getString("stallDescription"))
            }
        }catch(e: IOException){
            println("Error happen here")
        }
    }

    private fun populateList(): ArrayList<ModelFoodHorizontal> {

        val list = ArrayList<ModelFoodHorizontal>()
        if(myImageNameList.size> 0){
            for (i in 0..myImageList.size-1) {
                val imageModel = ModelFoodHorizontal()
                imageModel.setNames(myImageNameList[i])
                imageModel.setImage_drawables(myImageList[i])
                imageModel.setDescriptions(myImageDescriptionList[i])
                imageModel.setFoodFocus(myImageFoodFocusList[i])
                imageModel.setStallName(stallnameInRecommendedList[i])
                list.add(imageModel)
            }
        }

        return list
    }

    private fun populateStallList(): ArrayList<ModelStallVertical> {

        val list = ArrayList<ModelStallVertical>()
        for (i in 0..stallMyImageList.size-1) {
            val imageModel = ModelStallVertical()
            imageModel.setNames(stallMyImageNameList[i])
            imageModel.setImage_drawables(stallMyImageList[i].toInt())
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
