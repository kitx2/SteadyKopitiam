package com.example.steadykopitiam

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.purchases.PurchasesActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_food_item.*
import kotlinx.android.synthetic.main.activity_home.drawerLayout
import kotlinx.android.synthetic.main.activity_home.navigationView
import kotlinx.android.synthetic.main.activity_home.toolbar
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.json.JSONArray
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class FoodItemActivity : AppCompatActivity() {

    var navigationPosition: Int = 0
    lateinit var kopitiamDBHelper: DBHelper
    var user = ArrayList<UserRecord>()
    private var userpassword : String? = ""
    private var useremail  : String? = ""
    private var awardedPoint : Int = 0
    private var username : String? = ""

    //Steady picks
    private var steadyPicksRecyclerView: RecyclerView? = null
    private var imageModelArrayList: ArrayList<ModelFoodHorizontal>? = null
    private var adapter: AdapterFoodViewHorizontal? = null
    private var foodNameInRecc : String = ""
    private var stallNameInRecc : String = ""
    private var foodPrice : String = ""


    //TODO: Update foodList
    private var myImageList = intArrayOf()
    private val myImageNameList = arrayListOf<String>()
    private val myImageDescriptionList = arrayListOf<String>()
    private val myImageFoodFocusList = arrayListOf<String>()
    private var stallnameInRecommendedList = arrayListOf<String>()
    private val myImageFoodPrice = arrayListOf<String>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_item)
        kopitiamDBHelper = DBHelper(this)

        readRecommendedjson()

        //TODO: update stall name
        val stallName : String = intent.getStringExtra("foodStall")
        setTitle(stallName)
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        username = preferences.getString("username","")
        initView()

        //TODO: Retrieve JSON and extract specific food item details
        val foodName : String = intent.getStringExtra("foodName")
        val foodProtein : String = intent.getStringExtra("foodProtein")
        val foodCarbs : String = intent.getStringExtra("foodCarbs")
        val foodFat : String = intent.getStringExtra("foodFat")
        val foodFibre : String = intent.getStringExtra("foodFibre")
        val foodVitamins : String = intent.getStringExtra("foodVitamins")
        val foodCalories : String = intent.getStringExtra("foodCalories")
        val foodDescription : String = intent.getStringExtra("foodDescription")
        val foodFocus : String = intent.getStringExtra("foodFocus")
        val foodDeductPrice : Double = intent.getDoubleExtra("foodDeductPrice",0.0)
        val foodExtraPrice : Double = intent.getDoubleExtra("foodExtraPrice",0.0)
        val foodDishType : String = intent.getStringExtra("foodDishType")
        val foodMinerals : String = intent.getStringExtra("foodMinerals")
        val foodBasePrice : Double = intent.getDoubleExtra("foodBasePrice",0.0)
        println("food price "+foodBasePrice)

        //Render relevant text
        foodLabel.text = foodName
        foodDescLabel.text = foodDescription

        //Handle foodImage rendering in ImageView
        val foodImage : String = intent.getStringExtra("foodImage")
        val PACKAGE_NAME: String = getApplicationContext().getPackageName();
        val imagePlaceholder : ImageView = findViewById(R.id.stallPlaceholder)
        var id = application.resources.getIdentifier(PACKAGE_NAME+":drawable/"+foodImage,null,null)
        var res: Drawable = application.resources.getDrawable(id)
        imagePlaceholder.setImageDrawable(res)

        // if user had order food before in the past 3 days
        var foodpricePref = getSharedPreferences("foodPriceIncPrefs",Context.MODE_PRIVATE)
        if(foodpricePref.getBoolean("isPriceIncrease",false)){
            Toast.makeText(this, "You have purchased "+foodName+"in the past 24 hours and price will be increase 50 cents ",Toast.LENGTH_SHORT).show()
        }

        // if user had choose food from recommendations list prompt a message to display 50 cents is discounted
        var foodFromRecoList = getSharedPreferences("IsReccFoodSelected",Context.MODE_PRIVATE)
        if(foodFromRecoList.getBoolean("ReccFoodIsSelected",false)){
            Toast.makeText(this, "You have select "+foodName+" from our recommendation list, Enjoy 50 cents discounted price!!  ",Toast.LENGTH_SHORT).show()
            var sharedPreForReccEditer = foodFromRecoList.edit()
            sharedPreForReccEditer.putBoolean("ReccFoodIsSelected",false)
            sharedPreForReccEditer.commit()
        }

        //TODO: Render the details into Nutrition display table
        this.foodLabel.text = foodName
        this.foodDescLabel.text = foodDescription
        this.foodCarbs.text = "Carbs - " + foodCarbs +"g"
        this.foodProtein.text = "Protein - "+ foodProtein +"g"
        this.foodCalories.text = "Calories - "+ foodCalories+"g"
        this.foodFat.text = "Fat - "+foodFat+ "g"
        this.foodFibre.text = "Fibre - " + foodFibre+"g"
        this.foodVitamin.text = "Vitamins - "+ foodVitamins+"g"
        this.foodMinerals.text = "Minerals - "+foodMinerals+"g"
        this.btnPurchase.text = "Purchase - $" + String.format("%.2f",foodBasePrice)

        //Steady Picks
        steadyPicksRecyclerView = findViewById(R.id.SteadyPicksRecyclerView)

        imageModelArrayList = populateList()
        Log.d("hjhjh", imageModelArrayList!!.size.toString() + "")
        adapter = AdapterFoodViewHorizontal(applicationContext, imageModelArrayList!!)
        steadyPicksRecyclerView!!.adapter = adapter
        steadyPicksRecyclerView!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)

        steadyPicksRecyclerView!!.addOnItemTouchListener(
            FoodItemActivity.RecyclerTouchListener(
                applicationContext,
                steadyPicksRecyclerView!!,
                object : FoodItemActivity.ClickListener {

                    override fun onClick(view: View, position: Int) {
                        //TODO: Start new Activity here

                        foodNameInRecc = imageModelArrayList!![position].getNames()
                        stallNameInRecc = imageModelArrayList!![position].getStallName()
                        foodPrice =  imageModelArrayList!![position].getFoodPrice()
                        var sharedPreForReccEditer = foodFromRecoList.edit()
                        sharedPreForReccEditer.putBoolean("ReccFoodIsSelected",true)
                        sharedPreForReccEditer.commit()

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
                        //Start new Activity
                        //TODO: Pass stall name to QR Activity

                        startActivity(myIntent)
                        finish()
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )

        //Render coin redemption panel
        loadCoinRedemptionLayout(foodBasePrice)
        val switch = findViewById<Switch>(R.id.coinSwitch)

        //TODO: Purchase food
        val btnPurchase : Button = findViewById(R.id.btnPurchase)
        btnPurchase.setOnClickListener {
            //TODO: how to determine the food price is extra or deduct or not

            userpassword = preferences.getString("userPassword", "")
            useremail = preferences.getString("userEmail", "")
            user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)
            //TODO: Validate wallet amount, else prompt user to top-up
            if(!user.equals(null) && user.get(0).accountBalance < foodBasePrice ){
                Toast.makeText(this, "Your account balance is insufficient to purchase food. Please top up your wallet.",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                if(!user.isNullOrEmpty()) {
                    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm:ss")
                    val curTime = simpleDateFormat.format(Date())
                    awardedPoint = (foodBasePrice * 10).toInt()
                    println("current Time is " + curTime)

                    var result = kopitiamDBHelper.createOrderSummary(
                        OrderSummaryRecord(curTime, awardedPoint, foodBasePrice, foodName, foodCarbs.toInt(), foodProtein.toInt(), foodFat.toInt(),
                            foodMinerals.toDouble(), foodVitamins.toDouble(), foodCalories.toInt(), foodFocus, foodFibre.toInt(), foodExtraPrice.toString()
                        )
                    )

                    var deductWallet: Boolean
                    var finalAccountBalance: Double
                    var finalAccountPoints: Int
                    var redeemedAmt: Double
                    var pointsConsumed: Int

                    //TODO: Persist purchase order in DB
                    //TODO: Store 10% rebate of spending amount in wallet
                    if(switch.isChecked) {
                        var userPoints = user.get(0).accountPoints
                        var pointsRequiredToPurchase = (foodBasePrice * 100).toInt()

                        if (userPoints >= pointsRequiredToPurchase) {
                            finalAccountBalance = user.get(0).accountBalance
                            finalAccountPoints = user.get(0).accountPoints + awardedPoint - pointsRequiredToPurchase

                            deductWallet = kopitiamDBHelper.updateUser(
                                UserRecord(user.get(0).username, user.get(0).gender, user.get(0).height, user.get(0).weight, user.get(0).bmi,
                                    user.get(0).age, user.get(0).email, finalAccountBalance, finalAccountPoints, user.get(0).user_carbs,
                                    user.get(0).user_calories, user.get(0).user_fat, user.get(0).user_fibre, user.get(0).user_minerals,
                                    user.get(0).user_vitamins, user.get(0).user_dailyActivies, user.get(0).user_protein, user.get(0).user_password, user.get(0).phoneNumber
                                )
                            )
                            pointsConsumed = pointsRequiredToPurchase
                            redeemedAmt = foodBasePrice

                            Toast.makeText(this, "Consumed points = " + pointsConsumed + "\nRedeemedAmt = " + String.format("%.2f",redeemedAmt), Toast.LENGTH_SHORT).show()

                        } else {
                            finalAccountBalance = (userPoints / 100.0) + (user.get(0).accountBalance - foodBasePrice)
                            finalAccountPoints = user.get(0).accountPoints + awardedPoint - userPoints

                            deductWallet = kopitiamDBHelper.updateUser(
                                UserRecord(user.get(0).username, user.get(0).gender, user.get(0).height, user.get(0).weight, user.get(0).bmi,
                                    user.get(0).age, user.get(0).email, finalAccountBalance, finalAccountPoints, user.get(0).user_carbs,
                                    user.get(0).user_calories, user.get(0).user_fat, user.get(0).user_fibre, user.get(0).user_minerals,
                                    user.get(0).user_vitamins, user.get(0).user_dailyActivies, user.get(0).user_protein, user.get(0).user_password, user.get(0).phoneNumber
                                )
                            )
                            pointsConsumed = userPoints
                            redeemedAmt = userPoints / 100.0

                            Toast.makeText(this, "Consumed points = " + pointsConsumed + "\nRedeemedAmt = " + String.format("%.2f",redeemedAmt), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        finalAccountBalance = user.get(0).accountBalance - foodBasePrice
                        finalAccountPoints = user.get(0).accountPoints + awardedPoint

                        deductWallet = kopitiamDBHelper.updateUser(
                            UserRecord(user.get(0).username, user.get(0).gender, user.get(0).height, user.get(0).weight, user.get(0).bmi,
                                user.get(0).age, user.get(0).email, finalAccountBalance, finalAccountPoints, user.get(0).user_carbs,
                                user.get(0).user_calories, user.get(0).user_fat, user.get(0).user_fibre, user.get(0).user_minerals,
                                user.get(0).user_vitamins, user.get(0).user_dailyActivies, user.get(0).user_protein, user.get(0).user_password, user.get(0).phoneNumber
                            )
                        )
                        pointsConsumed = 0
                        redeemedAmt = 0.00

                        Toast.makeText(this, "Consumed points = " + pointsConsumed + "\nRedeemedAmt = " + String.format("%.2f",redeemedAmt), Toast.LENGTH_SHORT).show()
                    }
//                    Toast.makeText(this, "Switch check state = " + switch.isChecked.toString(),Toast.LENGTH_SHORT).show()

                    //TODO - BONUS feature: Send sms for confirmation of order made
                    if (result && deductWallet) {
                        Toast.makeText(this, "Food has been added into order summary.", Toast.LENGTH_SHORT).show()
                        val myIntent = Intent(applicationContext, PurchasesActivity::class.java)
//                    myIntent.putExtra("foodName",foodName)
//                    myIntent.putExtra("foodPrice",foodBasePrice)
//                    myIntent.putExtra("coinEarced",awardedPoint)
                        startActivity(myIntent)
                        this.overridePendingTransition(0, 0)
                        finish()
                    }
                } else {
                    Toast.makeText(this, "Food has not been added into order summary.", Toast.LENGTH_SHORT).show()
                }
            }
            //Direct user to last order
        }
    }
    //  --- to read data from json file --- Jy ///
    private fun readRecommendedjson(){
        // retrieve order summary and time ordered if there is not any then recommened 4 foods from different stall

        // data to pass to stall activity, stall name, stall description and image id
        var orSum = kopitiamDBHelper.retrieveAllOrderSummary()
        if(orSum.size == 0 ){
            // read recommended all stall list of food
            try{
                for(i in 0..myImageList.size-1){
                    println(" TTTTT ")
                    if(i == 0 ){
                        println(" RRRRRR ")
                        var json: String?
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
                        var json: String?
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
                        var json: String?
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
                        var json: String?
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
                // means user eaten this food 3 days before
                if((cur.time - 86400000 ) < d.time){

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
            if(count>0) {

                myImageList = IntArray(count)
                var temp: Int = 0
                if (!addCarbs && temp < count) {
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

    fun loadCoinRedemptionLayout(foodBasePrice:Double) {
        //TODO: Retrieve available coins for redemption
        var checkedState: Boolean
        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        userpassword = preferences.getString("userPassword", "")
        useremail = preferences.getString("userEmail", "")
        user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)

        if(!user.isNullOrEmpty()) {
            val btnPurchase = findViewById<Button>(R.id.btnPurchase)
            val sw = findViewById<Switch>(R.id.coinSwitch)
            val coinAmountLabel = findViewById<TextView>(R.id.coinAmountLabel)
            val deductedLabel = findViewById<TextView>(R.id.deductedLabel)
            var userPoints = user.get(0).accountPoints
            var pointsRequiredToPurchase = (foodBasePrice * 100).toInt()
            if (userPoints >= pointsRequiredToPurchase) {
                coinAmountLabel.text = pointsRequiredToPurchase.toString()
                deductedLabel.text =
                    "[-S$" + String.format("%.2f", pointsRequiredToPurchase / 100.0) + "]"
            } else {
                coinAmountLabel.text = userPoints.toString()
                deductedLabel.text = "[-S$" + String.format("%.2f", userPoints / 100.0) + "]"
            }

            //TODO: Update discounted price in button.Text
            sw?.setOnCheckedChangeListener({ _, isChecked ->
                if (isChecked) {
                    if (userPoints >= pointsRequiredToPurchase) {
                        btnPurchase.text =
                            "Purchase - $" + String.format("%.2f",foodBasePrice - pointsRequiredToPurchase / 100.0)
                    } else {
                        btnPurchase.text = "Purchase - $" + String.format("%.2f",foodBasePrice - userPoints / 100.0)
                    }

                } else {
                    btnPurchase.text = "Purchase - $" + String.format("%.2f",foodBasePrice)
                }
            })
        }
    }

    private fun populateList(): ArrayList<ModelFoodHorizontal> {

        val list = ArrayList<ModelFoodHorizontal>()
        for (i in 0..myImageList.size-1) {
            val imageModel = ModelFoodHorizontal()
            imageModel.setNames(myImageNameList[i])
            imageModel.setImage_drawables(myImageList[i])
            imageModel.setDescriptions(myImageDescriptionList[i])
            imageModel.setFoodFocus((myImageFoodFocusList[i]))
            imageModel.setStallName(stallnameInRecommendedList[i])
            list.add(imageModel)
        }

//        val list = java.util.ArrayList<ModelFoodHorizontal>()
//        for (i in 0..myImageList.size-1) {
//            val imageModel = ModelFoodHorizontal()
//            imageModel.setNames(myImageNameList[i])
//            imageModel.setImage_drawables(myImageList[i])
//            imageModel.setDescriptions(myImageDescriptionList[i])
//            imageModel.setFoodFocus(myImageFoodFocusList[i])
//            imageModel.setStallName(stallnameInRecommendedList[i])
//            list.add(imageModel)

        return list
    }

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(context: Context, recyclerView: RecyclerView, private val clickListener: FoodItemActivity.ClickListener?) : RecyclerView.OnItemTouchListener {

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
