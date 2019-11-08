package com.example.steadykopitiam

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.ActionBar

import com.google.android.material.tabs.TabLayout
import com.google.common.collect.Table

class DBHelper(context: Context) : SQLiteOpenHelper (context,DATABASE_NAME,null,DATABASE_VERSION)
{

    companion object{
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "steadyKopitiam"

        private val SQL_CREATE_USER =
            "CREATE TABLE " + TableUserInfo.TABLE_USER + "(" +
            TableUserInfo.COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
            TableUserInfo.COLUMN_USER_PASSWORD + " TEXT NOT NULL," +
                    TableUserInfo.COLUMN_USER_USERNAME + " TEXT NOT NULL,"+
            TableUserInfo.COLUMN_USER_AGE + " TEXT NOT NULL," +
            TableUserInfo.COLUMN_USER_EMAIL + " TEXT NOT NULL," +
            TableUserInfo.COLUMN_USER_WEIGHT + " DOUBLE NOT NULL," +
            TableUserInfo.COLUMN_USER_GENDER + " TEXT NOT NULL," +
            TableUserInfo.COLUMN_USER_HEIGHT + " DOUBLE NOT NULL," +
            TableUserInfo.COLUMN_USER_ACCOUNTBALANCE + " DOUBLE," +
            TableUserInfo.COLUMN_USER_ACCOUNTPOINTS + " INTEGER," +
            TableUserInfo.COLUMN_USER_BMI + " DOUBLE NOT NULL," +
            TableUserInfo.COLUMN_USER_CARBS + " INTEGER NOT NULL," +
            TableUserInfo.COLUMN_USER_FAT + " INTEGER NOT NULL,"+
            TableUserInfo.COLUMN_USER_FIBRE + " INTEGER NOT NULL,"+
            TableUserInfo.COLUMN_USER_MINERALS + " DOUBLE NOT NULL," +
            TableUserInfo.COLUMN_USER_VITAMINS + " DOUBLE NOT NULL," +
            TableUserInfo.COLUMN_USER__DAILYACTIVITY + " TEXT," +
            TableUserInfo.COLUMN_USER_PROTEIN + " INTEGER NOT NULL," +
                    TableUserInfo.COLUMN_USER_PHONENUMBER + " TEXT NOT NULL," +
            TableUserInfo.COLUMN_USER_CALORIES + " INTEGER NOT NULL)"

        private val SQL_CREATE_FOOD =
            "CREATE TABLE " + TableFoodInfo.TABLE_FOOD + "(" +
                    TableFoodInfo.COLUMN_FOOD_ID + " INTEGER PRIMARY KEY," +
                    TableFoodInfo.COLUMN_FOOD_NAME + " TEXT NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_BASEPRICE + " DOUBLE NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_CALORIES + " INTEGER NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_CARBS + " INTEGER NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_DEDUCTEDPRICE + " DOUBLE NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_DESCRIPTION + " TEXT," +
                    TableFoodInfo.COLUMN_FOOD_DISHTYPE + " TEXT," +
                    TableFoodInfo.COLUMN_FOOD_EXTRAPRICE + " DOUBLE NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_FAT + " INTEGER NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_FIBRE + " INTEGER NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_FOODSTALL + " TEXT," +
                    TableFoodInfo.COLUMN_FOOD_IMAGE + " TEXT," +
                    TableFoodInfo.COLUMN_FOOD_MINERALS + " INTEGER NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_PROTEIN + " INTEGER NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_FOCUS + " TEXT NOT NULL," +
                    TableFoodInfo.COLUMN_FOOD_VITAMINS + " INTEGER NOT NULL)"




        private val SQL_CREATE_ORDERSUMMARY =
            "CREATE TABLE " + TableOrderSummaryInfo.TABLE_ORDERSUMMARY + "(" +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_ID + "INTEGER PRIMARY KEY," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_TIMEDARTE + " TEXT," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_AWARDEDPOINTS + " INTEGER," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_SUBTOTAL + " DOUBLE," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODCALORIES + " INTEGER," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFIBRE + " INTEGER," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODVITAMNS + " DOUBLE," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODMINERALS + " DOUBLE,"+
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFOCUS + " TEXT," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODNAME + " TEXT," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODPROTEIN + " INTEGER," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFAT + " INTEGER," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODEXTRAOPRICE + " TEXT," +
                    TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODCARBS + " INTEGER)"

        private val SQL_DELETE_USER = "DROP TABLE IF EXISTS " +
                TableUserInfo.TABLE_USER

        private val SQL_DELETE_FOOD = "DROP TABLE IF EXISTS " +
                TableFoodInfo.TABLE_FOOD

        private val SQL_DELETE_ORDERSUMMARY = "DROP TABLE IF EXISTS " +
                TableOrderSummaryInfo.TABLE_ORDERSUMMARY

    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_USER)
        db?.execSQL(SQL_CREATE_FOOD)
        db?.execSQL(SQL_CREATE_ORDERSUMMARY)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_USER)
        db?.execSQL(SQL_DELETE_FOOD)
        db?.execSQL(SQL_DELETE_ORDERSUMMARY)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        onUpgrade(db, oldVersion, newVersion)
    }

    // *** User table method ***
    fun insertUser(user:UserRecord):Boolean {
        val db = writableDatabase
        val values = ContentValues()

        values.put(TableUserInfo.COLUMN_USER_ACCOUNTBALANCE,user.accountBalance)
        values.put(TableUserInfo.COLUMN_USER_ACCOUNTPOINTS,user.accountPoints)
        values.put(TableUserInfo.COLUMN_USER_AGE,user.age)
        values.put(TableUserInfo.COLUMN_USER_BMI,user.bmi)
        values.put(TableUserInfo.COLUMN_USER_CALORIES,user.user_calories)
        values.put(TableUserInfo.COLUMN_USER_GENDER,user.gender)
        values.put(TableUserInfo.COLUMN_USER_HEIGHT,user.height)
        values.put(TableUserInfo.COLUMN_USER_WEIGHT,user.weight)
        values.put(TableUserInfo.COLUMN_USER_EMAIL,user.email)
        values.put(TableUserInfo.COLUMN_USER_CARBS,user.user_carbs)
        values.put(TableUserInfo.COLUMN_USER_FAT,user.user_fat)
        values.put(TableUserInfo.COLUMN_USER_FIBRE,user.user_fibre)
        values.put(TableUserInfo.COLUMN_USER_PROTEIN,user.user_protein)
        values.put(TableUserInfo.COLUMN_USER__DAILYACTIVITY,user.user_dailyActivies)
        values.put(TableUserInfo.COLUMN_USER_VITAMINS,user.user_vitamins)
        values.put(TableUserInfo.COLUMN_USER_MINERALS,user.user_minerals)
        values.put(TableUserInfo.COLUMN_USER_USERNAME,user.username)
        values.put(TableUserInfo.COLUMN_USER_PASSWORD,user.user_password)
        values.put(TableUserInfo.COLUMN_USER_PHONENUMBER,user.phoneNumber)
        val newRowID = db.insert(TableUserInfo.TABLE_USER,null,values)
        println("new Row Id " + newRowID)
        return true;
    }

    fun deleteUser(username:String):Boolean {
        val db = writableDatabase
        val selection = TableUserInfo.COLUMN_USER_USERNAME + " LIKE ?"
        val selectionArgs = arrayOf(username)
        db.delete(TableUserInfo.TABLE_USER,selection,selectionArgs)
        return true
    }

    fun readAllUser():ArrayList<UserRecord>{
        val user = ArrayList<UserRecord>()
        val db = writableDatabase
        println("Before writaDatabase ___&&&&" )

        var cursor: Cursor?
        try{
            cursor = db.rawQuery("select * from "+
            TableUserInfo.TABLE_USER,null )
        }catch(e:SQLiteException){
            db.execSQL(SQL_CREATE_USER)
            return ArrayList()
        }

        var username : String
        var gender : String
        var height : Double
        var weight : Double
        var bmi : Double
        var age : String
        var email : String
        var accountBalance : Double
        var accountPoints : Int
        var user_carbs : Int
        var user_calories : Int
        var user_fat : Int
        var user_fibre : Int
        var user_minerals : Double
        var user_vitamins : Double
        var user_dailyActivies : String
        var user_protein: Int
        var user_passeord : String
        var user_phoneNumber : String
        println("****** Hello World **** ")

        if(cursor!!.moveToFirst()){
            while(cursor.isAfterLast==false){
                username = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_USERNAME))
                gender = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_GENDER))
                height = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_HEIGHT))
                weight = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_WEIGHT))
                bmi = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_BMI))
                age = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_AGE))
                email =cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_EMAIL))
                accountBalance = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_ACCOUNTBALANCE))
                accountPoints = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_ACCOUNTPOINTS))
                user_carbs = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_CARBS))
                user_calories = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_CALORIES))
                user_minerals = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_MINERALS))
                user_fat = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_FAT))
                user_fibre = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_FIBRE))
                user_protein = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_PROTEIN))
                user_vitamins = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_VITAMINS))
                user_dailyActivies = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER__DAILYACTIVITY))
                user_passeord = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_PASSWORD))
                user_phoneNumber = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_PHONENUMBER))
                user.add(
                    UserRecord(username,gender,height,weight,bmi,age,email,
                    accountBalance,accountPoints,user_carbs ,user_calories,user_fat,user_fibre,user_minerals,
                        user_vitamins,user_dailyActivies,user_protein,user_passeord,user_phoneNumber))
                cursor.moveToNext()

            }
        }
        return user
    }

    fun readUser(email:String, password:String ):ArrayList<UserRecord>{
        val user = ArrayList<UserRecord>()
        val db = writableDatabase
        println("Before writaDatabase ___&&&&" )
        println("email " + email)
        println("password "+password )
        var cursor: Cursor?


        try{
            cursor = db.rawQuery("select * from "+
                    TableUserInfo.TABLE_USER,null)
//                    TableUserInfo.COLUMN_USER_EMAIL + " = " + email + " AND "
//                    + TableUserInfo.COLUMN_USER_PASSWORD + " = " + password + "",null )
        }catch(e:SQLiteException){
            return ArrayList()
        }

        var username : String
        var gender : String
        var height : Double
        var weight : Double
        var bmi : Double
        var age : String
        var email_db : String
        var accountBalance : Double
        var accountPoints : Int
        var user_carbs : Int
        var user_calories : Int
        var user_fat : Int
        var user_fibre : Int
        var user_minerals : Double
        var user_vitamins : Double
        var user_dailyActivies : String
        var user_protein: Int
        var user_passeord_db : String
        var user_phoneNumber : String
        println("****** Hello World **** ")

        if(cursor!!.moveToFirst()){
            while(cursor.isAfterLast==false){

                username = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_USERNAME))
                gender = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_GENDER))
                height = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_HEIGHT))
                weight = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_WEIGHT))
                bmi = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_BMI))
                age = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_AGE))
                email_db =cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_EMAIL))
                accountBalance = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_ACCOUNTBALANCE))
                accountPoints = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_ACCOUNTPOINTS))
                user_carbs = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_CARBS))
                user_calories = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_CALORIES))
                user_minerals = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_MINERALS))
                user_fat = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_FAT))
                user_fibre = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_FIBRE))
                user_protein = cursor.getInt(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_PROTEIN))
                user_vitamins = cursor.getDouble(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_VITAMINS))
                user_dailyActivies = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER__DAILYACTIVITY))
                user_passeord_db = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_PASSWORD))
                user_phoneNumber = cursor.getString(cursor.getColumnIndex(TableUserInfo.COLUMN_USER_PHONENUMBER))
                if(email.equals(email_db)&& password.equals(user_passeord_db)){
                    user.add(
                        UserRecord(username,gender,height,weight,bmi,age,email_db,
                            accountBalance,accountPoints,user_carbs ,user_calories, user_fat,user_fibre,user_minerals,
                           user_vitamins,user_dailyActivies,user_protein,user_passeord_db,user_phoneNumber))
                }
                cursor.moveToNext()
            }
        }
        return user
    }

    fun updateUser(user: UserRecord):Boolean {
        val db = this.writableDatabase
        var values = ContentValues()
        var userFetched = readUser(user.email, user.user_password)

        if(!userFetched.isNullOrEmpty()) {
            val selection = TableUserInfo.COLUMN_USER_EMAIL + " LIKE ?"
            val selectionArgs = arrayOf(user.email)

            values.put(TableUserInfo.COLUMN_USER_ACCOUNTBALANCE,user.accountBalance)
            values.put(TableUserInfo.COLUMN_USER_ACCOUNTPOINTS,user.accountPoints)
            values.put(TableUserInfo.COLUMN_USER_AGE,user.age)
            values.put(TableUserInfo.COLUMN_USER_BMI,user.bmi)
            values.put(TableUserInfo.COLUMN_USER_CALORIES,user.user_calories)
            values.put(TableUserInfo.COLUMN_USER_GENDER,user.gender)
            values.put(TableUserInfo.COLUMN_USER_HEIGHT,user.height)
            values.put(TableUserInfo.COLUMN_USER_WEIGHT,user.weight)
            values.put(TableUserInfo.COLUMN_USER_EMAIL,user.email)
            values.put(TableUserInfo.COLUMN_USER_CARBS,user.user_carbs)
            values.put(TableUserInfo.COLUMN_USER_FAT,user.user_fat)
            values.put(TableUserInfo.COLUMN_USER_FIBRE,user.user_fibre)
            values.put(TableUserInfo.COLUMN_USER_PROTEIN,user.user_protein)
            values.put(TableUserInfo.COLUMN_USER__DAILYACTIVITY,user.user_dailyActivies)
            values.put(TableUserInfo.COLUMN_USER_VITAMINS,user.user_vitamins)
            values.put(TableUserInfo.COLUMN_USER_MINERALS,user.user_minerals)
            values.put(TableUserInfo.COLUMN_USER_USERNAME,user.username)
            values.put(TableUserInfo.COLUMN_USER_PASSWORD,user.user_password)
            values.put(TableUserInfo.COLUMN_USER_PHONENUMBER,user.phoneNumber)

//            val retVal = db.update(TableUserInfo.TABLE_USER, values, "email = " + user.email, null)
            db.update(TableUserInfo.TABLE_USER,values,selection,selectionArgs)

            return true
        }
        return false
    }

    // *** End of User table method ***

    // *** Food Item table method *** //

//    fun createFood(food:FoodRecord):Boolean{
//        val db = writableDatabase
//        val values = ContentValues()
//        values.put(TableFoodInfo.COLUMN_FOOD_NAME,food.foodName)
//        values.put(TableFoodInfo.COLUMN_FOOD_BASEPRICE,food.baseprice)
//        values.put(TableFoodInfo.COLUMN_FOOD_CALORIES,food.calories)
//        values.put(TableFoodInfo.COLUMN_FOOD_CARBS,food.carbs)
//        values.put(TableFoodInfo.COLUMN_FOOD_DEDUCTEDPRICE,food.deductPrice)
//        values.put(TableFoodInfo.COLUMN_FOOD_DESCRIPTION,food.description)
//        values.put(TableFoodInfo.COLUMN_FOOD_DISHTYPE,food.dishType)
//        values.put(TableFoodInfo.COLUMN_FOOD_EXTRAPRICE,food.extraPrice)
//        values.put(TableFoodInfo.COLUMN_FOOD_FAT,food.fat)
//        values.put(TableFoodInfo.COLUMN_FOOD_FIBRE,food.fibre)
//        values.put(TableFoodInfo.COLUMN_FOOD_FOODSTALL,food.stall)
//        values.put(TableFoodInfo.COLUMN_FOOD_MINERALS,food.minerals)
//        values.put(TableFoodInfo.COLUMN_FOOD_PROTEIN,food.protein)
//        values.put(TableFoodInfo.COLUMN_FOOD_VITAMINS,food.vitamuns)
//        values.put(TableFoodInfo.COLUMN_FOOD_FOCUS,food.focus)
//        val newRowId = db.insert(TableFoodInfo.TABLE_FOOD,null,values)
//        return true;
//    }


    // end of Food Item Method

    // Start of Order Summary Method
    fun createOrderSummary(orderSummary:OrderSummaryRecord):Boolean{
        val db = writableDatabase
        val values = ContentValues()

        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_AWARDEDPOINTS,orderSummary.orderSummaryAwardedPoints)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_SUBTOTAL,orderSummary.orderSummarySubtotal)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_TIMEDARTE,orderSummary.orderSummaryTimeDate)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODCALORIES,orderSummary.orderSummaryCalories)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODCARBS,orderSummary.orderSummaryFoodCarbs)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFAT,orderSummary.orderSummaryFat)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFIBRE,orderSummary.orderSummaryFibre)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFOCUS,orderSummary.orderSummaryFocus)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODVITAMNS,orderSummary.orderSummaryVitamins)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODPROTEIN,orderSummary.orderSummaryProtein)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODNAME,orderSummary.orderSummaryFoodName)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODMINERALS,orderSummary.orderSummaryMinerals)
        values.put(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODEXTRAOPRICE,orderSummary.orderSummaryExtraPrice)

        db.insert(TableOrderSummaryInfo.TABLE_ORDERSUMMARY,null,values)
        return true;
    }

    fun retrieveAllOrderSummary() : ArrayList<OrderSummaryRecord>{
        val listOrder = ArrayList<OrderSummaryRecord>()
        val db = writableDatabase


        var cursor: Cursor?
        try{
            cursor = db.rawQuery("select * from "+
                    TableOrderSummaryInfo.TABLE_ORDERSUMMARY,null )
        }catch(e:SQLiteException){
            db.execSQL(SQL_CREATE_USER)
            return ArrayList()
        }

        var orderSummaryTimeDate : String
        var orderSummaryAwardedPoints : Int
        var orderSummarySubtotal : Double
        var orderSummaryFoodName  : String
        var orderSummaryFoodCarbs : Int
        var orderSummaryProtein : Int
        var orderSummaryFat : Int
        var orderSummaryMinerals : Double
        var orderSummaryVitamins : Double
        var orderSummaryCalories : Int
        var orderSummaryFocus : String
        var orderSummaryFibre : Int
        var orderSummaryExtraPrice: String

        if(cursor!!.moveToFirst()){
            while(cursor.isAfterLast==false){
                orderSummaryAwardedPoints = cursor.getInt(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_AWARDEDPOINTS))
                orderSummaryCalories = cursor.getInt(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODCALORIES))
                orderSummaryFat = cursor.getInt(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFAT))
                orderSummaryFibre = cursor.getInt(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFIBRE))
                orderSummaryFocus = cursor.getString(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODFOCUS))
                orderSummaryFoodCarbs = cursor.getInt(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODCARBS))
                orderSummaryFoodName = cursor.getString(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODNAME))
                orderSummaryMinerals = cursor.getDouble(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODMINERALS))
                orderSummaryProtein = cursor.getInt(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODPROTEIN))
                orderSummarySubtotal = cursor.getDouble(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_SUBTOTAL))
                orderSummaryTimeDate = cursor.getString(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_TIMEDARTE))
                orderSummaryVitamins = cursor.getDouble(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODVITAMNS))
                orderSummaryExtraPrice = cursor.getString(cursor.getColumnIndex(TableOrderSummaryInfo.COLUMN_ORDERSUMMARY_FOODEXTRAOPRICE))

                listOrder.add(
                    OrderSummaryRecord(orderSummaryTimeDate,orderSummaryAwardedPoints,orderSummarySubtotal,orderSummaryFoodName,orderSummaryFoodCarbs,orderSummaryProtein
                    ,orderSummaryFat,orderSummaryMinerals,orderSummaryVitamins,orderSummaryCalories,orderSummaryFocus,orderSummaryFibre,orderSummaryExtraPrice))
                cursor.moveToNext()

            }
        }
        return listOrder
    }






    // End of Food Order Summary



}