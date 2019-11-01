package com.example.steadykopitiam

import android.provider.BaseColumns



class TableCartInfo:BaseColumns {

    companion object{
        val TABLE_CART = "cart"
        val COLUMN_CART_ID = "_id"
        val COLUMN_FOOD_ID = "cart_food_id"
        val COLUMN_CART_SUBTOTAL = "cart_subtotal"
        val COLUMN_CART_TOTAL = "cart_total"
    }

}