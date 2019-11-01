package com.example.steadykopitiam

import android.provider.BaseColumns

class TableOrderSummaryInfo:BaseColumns {
    companion object{
        val TABLE_ORDERSUMMARY = "ordersummary"
        val COLUMN_ORDERSUMMARY_ID = "_id"
        val COLUMN_FOOD_ID = "ordersummary_food_id"
        val COLUMN_ORDERSUMMARY_TIMEDARTE = "ordersummary_timedate"
        val COLUMN_ORDERSUMMARY_SUBTOTAL = "ordersummary_subtotal"
        val COLUMN_ORDERSUMMARY_DISCOUNT = "ordersummary_discount"
        val COLUMN_ORDERSUMMARY_AWARDEDPOINTS = "ordersummary_awardedpoints"
    }
}