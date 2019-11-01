package com.example.steadykopitiam

import android.provider.BaseColumns



class TableFoodInfo:BaseColumns {

    companion object{

        val TABLE_FOOD = "food"
        val COLUMN_FOOD_ID = "_id"
        val COLUMN_FOOD_NAME = "food_name"
        val COLUMN_FOOD_PROTEIN = "food_protein"
        val COLUMN_FOOD_FAT = "food_fat"
        val COLUMN_FOOD_CARBS = "food_carbs"
        val COLUMN_FOOD_DESCRIPTION = "food_description"
        val COLUMN_FOOD_IMAGE = "food_image"
        val COLUMN_FOOD_BASEPRICE = "food_baseprice"
        val COLUMN_FOOD_FIBRE = "food_fibre"
        val COLUMN_FOOD_MINERALS = "food_minerals"
        val COLUMN_FOOD_VITAMINS = "food_vitamins"
        val COLUMN_FOOD_EXTRAPRICE = "food_extraprice"
        val COLUMN_FOOD_DEDUCTEDPRICE = "food_deductedprice"
        val COLUMN_FOOD_FOODSTALL = "food_stall"
        val COLUMN_FOOD_CALORIES = "food_calories"
        val COLUMN_FOOD_DISHTYPE = "food_dishType"

    }
}