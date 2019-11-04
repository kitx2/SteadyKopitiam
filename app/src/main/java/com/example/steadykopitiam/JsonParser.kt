package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JsonParser(private var c: Context, private var jsonData: String,private var foodName : String, private var foodStall : String, private var foodPrice: String) : AsyncTask<Void, Void, Boolean>() {

    val foodRecord = FoodRecord()
    private var size : Int = 0

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): Boolean {
        var fObj = parse()
        if(!fObj.equals(null)){
            println("Sohai !!!!")
            return true
        }else{
            println(" Not Sohai !!!!")
            return false
        }

    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
        if (result!!) {
            // create order summary here  -- go to confirm page and pass data into it
            val myIntent = Intent(c,FoodItemActivity::class.java)
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
            c.startActivity(myIntent)

        } else {
            Toast.makeText(c, "unable to parse", Toast.LENGTH_LONG).show()
            Toast.makeText(
                c, "this is the data we were trying to parse : " +
                        jsonData, Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun parse() : FoodRecord {

        try {
            val ja = JSONArray(jsonData)
            size = JSONArray(jsonData).length()
            println("size of the json retrieve from qr code "+ja.length())
            // to find which food is selected
            for(i in 0..ja.length()-1){
                var jsonObject = ja.getJSONObject(i)
                // pass the data to food Record obj
                println("food get from internet "+jsonObject.getString("foodName"))
                if(jsonObject.getString("foodName").equals(foodName)){
                    println("get the same food name hahahahahahaah")
                    foodRecord.setFoodNames(jsonObject.getString("foodName"))
                    foodRecord.setProtein(jsonObject.getString("foodProtein"))
                    foodRecord.setCalories(jsonObject.getString("foodCalories"))
                    foodRecord.setBasePrice(jsonObject.getString("foodBasePrice").toDouble())
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
                }
            }
            return foodRecord
        } catch (e: JSONException) {
            e.printStackTrace()
            return foodRecord
        }
    }

//    class User(
//        private var m_username: String,
//        private var m_name: String,
//        private var m_email: String,
//        private var m_city: String
//    ) {
//        fun getUsername(): String {
//            return m_username
//        }
//
//        fun getName(): String {
//            return m_name
//        }
//
//        fun getEmail(): String {
//            return m_email
//        }
//
//        fun getCity():String{
//            return m_city
//        }
//
//
//    }
}