package com.example.steadykopitiam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.steadykopitiam.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(){


    var choice = arrayOf("High","Medium","Low")
    lateinit var kopitiamDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        kopitiamDBHelper = DBHelper(this)


        val spinner = findViewById<Spinner>(R.id.spinnerChoice)
        var dailtAct : String = ""
        if (spinner != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, choice)
            spinner.adapter = arrayAdapter
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    dailtAct = choice[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        println("daily Activity is "+ dailtAct )
        val btn_to_register = findViewById<Button>(R.id.btn_to_register)
        btn_to_register.setOnClickListener {
            registerUser(dailtAct)
            val myIntent = Intent(this,HomeActivity::class.java)
            startActivity(myIntent)
        }

    }

    fun registerUser(dailyAct:String){

        var username  = this.register_name.text.toString()
        var phoneNumber = this.register_phoneNumber.text.toString()
        var password  = this.register_password.text.toString()
        var email     = this.register_email.text.toString()
        var age       = this.register_age.text.toString()
        var height    = this.register_height.text.toString().toDouble()
        var weight    = this.register_weight.text.toString().toDouble()
        var bmi = (weight / (height*height))
        var calories : Int = 0
        var bmr = (10 * weight) + (6.25 * height) - (5 * age.toInt())
        var gender      : String = ""
        var accountBalance : String = "20"
        var accountPoints : String = "0"

        if(this.register_male.text.toString()!= ""){
            gender = "MALE"
        }else {
            gender = "FEMALE"
        }
        if(dailyAct.equals("High")){
            calories = (bmr * 1.7).toInt()
        }else if(dailyAct.equals("Medium")){
            calories = (bmr * 1.5).toInt()
        }else{
            calories = (bmr * 1.2).toInt()
        }

        var carb = ( calories * 0.6 / 4 )
        var protein = (calories * 0.125 / 4 )
        var minerails = "3.4"
        var fibra = "30"
        var vitamins = "0.09"
        var fat = (calories * 0.275 / 9 )

        var result = kopitiamDBHelper.insertUser(UserRecord(username,gender,height,weight,bmi.toDouble(),age,email,accountBalance.toInt(),accountPoints.toInt(),carb.toInt(),calories,
            fat.toInt(),fibra.toInt(),minerails.toDouble(),vitamins.toDouble(),dailyAct,protein.toInt(),password,phoneNumber))


        Toast.makeText(this, "Added User : "+result, Toast.LENGTH_LONG).show()
    }
}
