package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.widget.TextView
import android.widget.Toast
import com.example.steadykopitiam.ui.home.HomeActivity
import com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParser(private var c: Context, private var jsonData: String,private var foodName : String, private var foodStall : String, private var foodPrice: String) : AsyncTask<Void, Void, Boolean>() {

    val foodRecord = FoodRecord()
    private var size : Int = 0

    // sharedPref to check if price increase
    lateinit  var sharedPref : SharedPreferences
    // sharedPred to check if recommended food choose then price deducted
    lateinit var foodFromRecoList : SharedPreferences

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): Boolean {
        return parse()

    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        val myIntent = Intent(c.applicationContext,FoodItemActivity::class.java)

        if (result!!) {
            // create order summary here  -- go to confirm page and pass data into it
            var fr = FoodRecord()
            myIntent.putExtra("foodName",foodRecord.getFoodNames())
            myIntent.putExtra("foodStall",foodRecord.getStall())
            myIntent.putExtra("foodBasePrice",foodRecord.getBasePrice())
            myIntent.putExtra("foodProtein",foodRecord.getProtein())
            myIntent.putExtra("foodCarbs",foodRecord.getCarbs())
            myIntent.putExtra("foodFat",foodRecord.getFat())
            myIntent.putExtra("foodFibre",foodRecord.getFibre())
            myIntent.putExtra("foodMinerals",foodRecord.getMinerals())
            myIntent.putExtra("foodCalories",foodRecord.getCalories())
            myIntent.putExtra("foodDescription",foodRecord.getDescription())
            myIntent.putExtra("foodFocus",foodRecord.getFocus())
            myIntent.putExtra("foodDeductPrice",foodRecord.getDeductedPrice())
            myIntent.putExtra("foodExtraPrice",foodRecord.getExtraPrice())
            myIntent.putExtra("foodDishType",foodRecord.getDishType())
            myIntent.putExtra("foodVitamins",foodRecord.getVitamins())
            myIntent.putExtra("foodImage",foodRecord.getImage())
            println("Food price !!!!!!!!!!!!!!"+ foodRecord.getBasePrice())
            println("Food price DeDucted &&&&&&!!!!!!"+ foodRecord.getDeductedPrice())
            myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            c.applicationContext.startActivity(myIntent)
        } else {
            Toast.makeText(c, "unable to parse", Toast.LENGTH_LONG).show()
            Toast.makeText(
                c, "this is the data we were trying to parse : " +
                        jsonData, Toast.LENGTH_LONG
            ).show()
        }

    }
    // --- to read the food information correctly and handle price based on past order --- Jy
    private fun parse() : Boolean {

        try {
            val ja = JSONArray(jsonData)
            size = JSONArray(jsonData).length()

            // to find which food is selected
            for(i in 0..ja.length()-1){
                var jsonObject = ja.getJSONObject(i)

                if(jsonObject.getString("foodName").equals(foodName)){
                    sharedPref = c?.getSharedPreferences("foodPriceIncPrefs",Context.MODE_PRIVATE)
                    foodFromRecoList = c?.getSharedPreferences("IsReccFoodSelected",Context.MODE_PRIVATE)

                    // check if price increase
                    if(sharedPref.getBoolean("isPriceIncrease",false)){
                        foodRecord.setBasePrice(jsonObject.getString("foodExtraPrice").toDouble())
                    }
                      // if recommnaded food is selected
                    else if(foodFromRecoList.getBoolean("ReccFoodIsSelected",false)){
                        foodRecord.setBasePrice(jsonObject.getString("fooddeductPrice").toDouble())
                        println("food recc is true ")
                    }
                    else{
                        foodRecord.setBasePrice(jsonObject.getString("foodBasePrice").toDouble())
                    }
                    foodRecord.setFoodNames(jsonObject.getString("foodName"))
                    foodRecord.setProtein(jsonObject.getString("foodProtein"))
                    foodRecord.setCalories(jsonObject.getString("foodCalories"))
                    foodRecord.setCarbs(jsonObject.getString("foodCarbs"))
                    foodRecord.setDeductedPrice(jsonObject.getString("fooddeductPrice").toDouble())
                    foodRecord.setDescription(jsonObject.getString("foodDescription"))
                    foodRecord.setDishType(jsonObject.getString("foodDishType"))
                    foodRecord.setExtraPrice(jsonObject.getString("foodExtraPrice").toDouble())
                    foodRecord.setFat(jsonObject.getString("foodFat"))
                    foodRecord.setFibre(jsonObject.getString("foodFibre"))
                    foodRecord.setFocus(jsonObject.getString("foodFocus"))
                    foodRecord.setMinerals(jsonObject.getString("foodMinerals"))
                    foodRecord.setStall(jsonObject.getString("foodStall"))
                    foodRecord.setVitamins(jsonObject.getString("foodVitamins"))
                    foodRecord.setImage(jsonObject.getString("foodResourceId"))
                }
            }
            return true
        } catch (e: JSONException) {
            e.printStackTrace()
            return false
        }
    }

}