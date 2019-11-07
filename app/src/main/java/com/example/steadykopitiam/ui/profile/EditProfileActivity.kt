package com.example.steadykopitiam.ui.profile

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.steadykopitiam.DBHelper
import com.example.steadykopitiam.R
import com.example.steadykopitiam.UserRecord
import com.example.steadykopitiam.ui.home.HomeActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_age
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_email
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_height
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_name
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_password
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_phoneNumber
import kotlinx.android.synthetic.main.activity_register.reg_input_layout_weight
import kotlinx.android.synthetic.main.activity_register.register_age
import kotlinx.android.synthetic.main.activity_register.register_email
import kotlinx.android.synthetic.main.activity_register.register_height
import kotlinx.android.synthetic.main.activity_register.register_name
import kotlinx.android.synthetic.main.activity_register.register_password
import kotlinx.android.synthetic.main.activity_register.register_phoneNumber
import kotlinx.android.synthetic.main.activity_register.register_weight

class EditProfileActivity : AppCompatActivity() {
    lateinit var kopitiamDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        var choice = arrayOf("High","Medium","Low")
        val btnUpdateProfile : Button = findViewById(R.id.btnUpdateProfile)
        val spinner = findViewById<Spinner>(R.id.spinnerChoice)
        var genderStr : String = ""
        var dailyAct : String = ""

        val reg_input_layout_email: TextInputLayout = findViewById(R.id.reg_input_layout_email)
        val register_email : EditText = findViewById(R.id.register_email)
        val reg_input_layout_phoneNumber: TextInputLayout = findViewById(R.id.reg_input_layout_phoneNumber)
        val register_phoneNumber : EditText = findViewById(R.id.register_phoneNumber)
        val reg_input_layout_name: TextInputLayout = findViewById(R.id.reg_input_layout_name)
        val register_name : EditText = findViewById(R.id.register_name)
        val reg_input_layout_password: TextInputLayout = findViewById(R.id.reg_input_layout_password)
        val register_password : EditText = findViewById(R.id.register_password)
        val reg_input_layout_age: TextInputLayout = findViewById(R.id.reg_input_layout_age)
        val register_age : EditText = findViewById(R.id.register_age)
        val reg_input_layout_weight: TextInputLayout = findViewById(R.id.reg_input_layout_weight)
        val register_weight : EditText = findViewById(R.id.register_weight)
        val reg_input_layout_height: TextInputLayout = findViewById(R.id.reg_input_layout_height)
        val register_height : EditText = findViewById(R.id.register_height)
        val radio_group : RadioGroup = findViewById(R.id.radio_group)

        //Text validation
        validation()

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
                    (parent.getChildAt(0) as TextView).setTextColor(Color.rgb(0,150,136))
                    (parent.getChildAt(0) as TextView).textSize = 18f
                    dailyAct = choice[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        // Get radio group selected item
        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                genderStr = radio.text.toString()
            })

        btnUpdateProfile.setOnClickListener {
            var valid : Boolean = true
            if(register_email.text.isEmpty()) {
                reg_input_layout_email.setError("Please enter your email.")
                valid = false
            }
            if(register_password.text.isEmpty()) {
                reg_input_layout_password.setError("Please enter your password.")
                valid = false
            }
            if (register_phoneNumber.text.isEmpty()) {
                reg_input_layout_phoneNumber.setError("Please enter your phone number.")
                valid = false
            }
            if (register_age.text.isEmpty()) {
                reg_input_layout_age.setError("Please enter your age.")
                valid = false
            }
            if (register_name.text.isEmpty()) {
                reg_input_layout_name.setError("Please enter your name.")
                valid = false
            }
            if (register_height.text.isEmpty()) {
                reg_input_layout_height.setError("Please enter your height.")
                valid = false
            }
            if (register_weight.text.isEmpty()) {
                reg_input_layout_weight.setError("Please enter your weight.")
                valid = false
            }
            if(valid) {
                updateUser(dailyAct, genderStr)
                val myIntent = Intent(this, EditProfileActivity::class.java)
                startActivity(myIntent)
                this.overridePendingTransition(0, 0)
                finish()
            }

        }

    }

    fun updateUser(dailyAct:String, genderString: String){

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
        var gender      : String = genderString
        var accountBalance : String = "20"
        var accountPoints : String = "0"


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

        var result = kopitiamDBHelper.insertUser(
            UserRecord(username,gender,height,weight,bmi.toDouble(),age,email,accountBalance.toInt(),accountPoints.toInt(),carb.toInt(),calories,
                fat.toInt(),fibra.toInt(),minerails.toDouble(),vitamins.toDouble(),dailyAct,protein.toInt(),password,phoneNumber)
        )
        Toast.makeText(this, "Added User : "+result, Toast.LENGTH_LONG).show()
    }

    fun validation() {

        register_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_email.setError("Please enter your email.")
                else
                    reg_input_layout_email.setError(null)
            }
        })

        register_phoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_phoneNumber.setError("Please enter your phone number.")
                else
                    reg_input_layout_phoneNumber.setError(null)
            }
        })

        register_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_name.setError("Please enter your name.")
                else
                    reg_input_layout_name.setError(null)
            }
        })

        register_password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_password.setError("Please enter your password.")
                else
                    reg_input_layout_password.setError(null)
            }
        })

        register_age.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_age.setError("Please enter your age.")
                else
                    reg_input_layout_age.setError(null)
            }
        })

        register_height.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_height.setError("Please enter your height.")
                else
                    reg_input_layout_height.setError(null)
            }
        })

        register_weight.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (s.length == 0)
                    reg_input_layout_weight.setError("Please enter your weight.")
                else
                    reg_input_layout_weight.setError(null)
            }
        })

    }
}
