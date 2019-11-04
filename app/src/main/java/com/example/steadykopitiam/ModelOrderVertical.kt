package com.example.steadykopitiam

import android.R.attr.name
import android.R.attr.src


class ModelOrderVertical {


    var name: String? = null
    var oDate: String? = null
    var point: String? = null
    var price: String? = null


    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getDates(): String {
        return oDate.toString()
    }

    fun setDates(oDate: String) {
        this.oDate = oDate
    }

    fun getPoints(): String {
        return point.toString()
    }

    fun setPoints(point: String) {
        this.point = point
    }

    fun getPrices(): String {
        return price.toString()
    }

    fun setPrices(price: String) {
        this.price = price
    }



}