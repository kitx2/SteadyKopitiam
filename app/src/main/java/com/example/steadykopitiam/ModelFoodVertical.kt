package com.example.steadykopitiam

import android.R.attr.name
import android.R.attr.src


class ModelFoodVertical {


    var name: String? = null
    var description: String? = null
    var price: String? = null
    var image_drawable: Int = 0



    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getDescriptions(): String {
        return description.toString()
    }

    fun setDescriptions(description: String) {
        this.description = description
    }

    fun getPrices(): String {
        return price.toString()
    }

    fun setPrices(price: String) {
        this.price = price
    }

    fun getImage_drawables(): Int {
        return image_drawable
    }

    fun setImage_drawables(image_drawable: Int) {
        this.image_drawable = image_drawable
    }

}