package com.example.steadykopitiam

import android.provider.BaseColumns

class TableOrderSummaryInfo:BaseColumns {
    companion object{
        val TABLE_ORDERSUMMARY = "ordersummary"
        val COLUMN_ORDERSUMMARY_ID = "_id"
        val COLUMN_ORDERSUMMARY_TIMEDARTE = "ordersummary_timedate"
        val COLUMN_ORDERSUMMARY_SUBTOTAL = "ordersummary_subtotal"
        val COLUMN_ORDERSUMMARY_AWARDEDPOINTS = "ordersummary_awardedpoints"
        val COLUMN_ORDERSUMMARY_FOODNAME = "foodName"
        val COLUMN_ORDERSUMMARY_FOODCARBS = "foodCarb"
        val COLUMN_ORDERSUMMARY_FOODPROTEIN = "foodProtein"
        val COLUMN_ORDERSUMMARY_FOODFAT = "foodFat"
        val COLUMN_ORDERSUMMARY_FOODFIBRE = "foodFibre"
        val COLUMN_ORDERSUMMARY_FOODMINERALS = "foodMinerals"
        val COLUMN_ORDERSUMMARY_FOODVITAMNS = "foodVitamins"
        val COLUMN_ORDERSUMMARY_FOODCALORIES  = "foodCalories"
        val COLUMN_ORDERSUMMARY_FOODFOCUS = "foodFocus"
    }
}