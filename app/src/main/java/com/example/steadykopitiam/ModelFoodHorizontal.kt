package com.example.steadykopitiam

import android.R.attr.name



class ModelFoodHorizontal {

    var name: String? = null
    var description: String? = null
    var image_drawable: Int = 0
    var foodfocus: String? = null
    var foodStallName : String? = null
    var selectedfoodPrice : String? = null

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

    fun getFoodFocus(): String {
        return foodfocus.toString()
    }

    fun setFoodFocus(foodfocus: String) {
        this.foodfocus = foodfocus
    }

    fun getImage_drawables(): Int {
        return image_drawable
    }

    fun setImage_drawables(image_drawable: Int) {
        this.image_drawable = image_drawable
    }

    fun getStallName() :String {
        return foodStallName.toString()
    }

    fun setStallName(stallName:String){
        this.foodStallName = stallName
    }

    fun getFoodPrice():String {
        return selectedfoodPrice.toString()
    }

    fun setFoodPrice(foodPrice : String ){
        this.selectedfoodPrice = foodPrice
    }

}