package com.example.steadykopitiam

class FoodRecord(){

    var foodName: String? = null
    private var protein: String? = ""
    private var fat: String? = ""
    private var carbs: String? = ""
    private var vitamins: String? = ""
    private var fibre: String? = ""
    private var minerals: String? = ""
    private var calories: String? = ""
    private var description: String? = ""
    private var baseprice: Double = 0.0
    private var extraPrice: Double = 0.0
    private var deductPrice: Double = 0.0
    private var stall: String? = ""
    private var dishType: String? = ""
    private var focus: String? = ""




    fun getFoodNames(): String {
        return foodName.toString()
    }

    fun setFoodNames(foodName: String) {
        this.foodName = foodName
    }

    fun getProtein(): String {
        return protein.toString()
    }

    fun setProtein(protein: String) {
        this.protein = protein
    }

    fun getFat(): String {
        return this.fat.toString()
    }

    fun setFat(fat: String) {
        this.fat = fat.toString()
    }

    fun getCarbs(): String {
        return carbs.toString()
    }

    fun setCarbs(carbs: String) {
        this.carbs = carbs
    }

    fun getVitamins(): String {
        return vitamins.toString()
    }

    fun setVitamins(vitamins : String ){
        this.vitamins = vitamins
    }

    fun getFibre(): String {
        return fibre.toString()
    }

    fun setFibre(fibre : String ){
        this.fibre = fibre
    }

    fun getMinerals(): String{
        return minerals.toString()
    }

    fun setMinerals(minerals : String ){
        this.minerals = minerals
    }

    fun getCalories(): String {
        return calories.toString()
    }

    fun setCalories(calories : String ){
        this.calories = calories
    }

    fun getDescription() :String {
        return description.toString()
    }

    fun setDescription(description : String ){
        this.description = description
    }

    fun getBasePrice():Double {
        return baseprice.toDouble()
    }

    fun setBasePrice(basePrice : Double ){
        this.baseprice = baseprice
    }

    fun getExtraPrice() : Double {
        return extraPrice.toDouble()
    }

    fun setExtraPrice(extraPrice : Double ){
        this.extraPrice = extraPrice
    }

    fun getDeductedPrice():Double {
        return deductPrice.toDouble()
    }

    fun setDeductedPrice(deductedPrice:Double){
        this.deductPrice = deductPrice
    }

    fun getStall(): String {
        return stall.toString()
    }

    fun setStall(stallName: String ){
        this.stall = stallName
    }

    fun getDishType():String{
        return dishType.toString()
    }

    fun setDishType(dishType: String ){
        this.dishType
    }

    fun getFocus():String {
        return focus.toString()
    }

    fun setFocus(focus :String ){
        this.focus = focus
    }
}

