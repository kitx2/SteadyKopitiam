package com.example.steadykopitiam

import android.provider.BaseColumns

class TableUserInfo:BaseColumns {

    companion object{
        val TABLE_USER = "user"
        val COLUMN_USER_ID = "_id"
        val COLUMN_USER_USERNAME = "username"
        val COLUMN_USER_GENDER = "gender"
        val COLUMN_USER_HEIGHT = "height"
        val COLUMN_USER_WEIGHT = "weight"
        val COLUMN_USER_BMI = "bmi"
        val COLUMN_USER_AGE = "age"
        val COLUMN_USER_EMAIL = "email"
        val COLUMN_USER_ACCOUNTBALANCE = "accountbalance"
        val COLUMN_USER_ACCOUNTPOINTS = "accountpoints"
        val COLUMN_USER_CARBS = "user_carbs"
        val COLUMN_USER_CALORIES = "user_calories"
        val COLUMN_USER_PROTEIN = "user_protein"
        val COLUMN_USER_VITAMINS = "user_vitamins"
        val COLUMN_USER_MINERALS = "user_minerals"
        val COLUMN_USER_FIBRE = "user_fibre"
        val COLUMN_USER_FAT = "user_fat"
        val COLUMN_USER__DAILYACTIVITY = "user_dailyActivity"
        val COLUMN_USER_PASSWORD = "user_password"
        val COLUMN_USER_PHONENUMBER = "user_phonenumber"
    }
}