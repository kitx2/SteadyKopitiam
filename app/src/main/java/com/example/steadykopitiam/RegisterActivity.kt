package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.steadykopitiam.ui.home.HomeActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern.compile


class RegisterActivity : AppCompatActivity(){

    var choice = arrayOf("High","Medium","Low")
    lateinit var kopitiamDBHelper: DBHelper
    lateinit var sharedPreferences : SharedPreferences
    private val emailRegex = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        kopitiamDBHelper = DBHelper(this)
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btn_to_register = findViewById<Button>(R.id.btn_to_register)
        val spinner = findViewById<Spinner>(R.id.spinnerChoice)
        var genderStr : String = ""
        var dailtAct : String = ""

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
        val reg_bmi : EditText = findViewById(R.id.profile_bmi)
        val reg_bmiStatus : TextView = findViewById(R.id.bmiStatus)
        reg_bmi.setEnabled(false)
        reg_bmi.setTextColor(Color.parseColor("#8e8e8e"))
        reg_bmi.setBackgroundColor(Color.TRANSPARENT)


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
                    dailtAct = choice[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }

        // Get radio group selected item
        radio_group.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                genderStr = radio.text.toString()
            })

        register_height.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val heightStr    = register_height.text.toString()
                val weightStr    = register_weight.text.toString()
                var status:String
                var height: String
                var weight: String
                var bmi: Double

                if (heightStr.isBlank() || heightStr.startsWith(".")) {
                    height = "0"
                } else {
                    height = heightStr
                }
                if (weightStr.isBlank() || weightStr.startsWith(".")) {
                    weight = "0"
                } else {
                    weight = weightStr
                }
                bmi = weight.toDouble() / (height.toDouble() * height.toDouble())
                if(bmi.isInfinite() || bmi.isNaN()) {
                    bmi = 0.0
                    status = ""
                } else if(bmi < 18.5) {
                    status = "(Underweight)"
                } else if (bmi >= 18.5 && bmi <= 22.9) {
                    status = "(Normal)"
                } else if (bmi >= 23.0 && bmi <= 27.4) {
                    status = "(Pre-obesity)"
                } else {
                    status = "(Obesity)"
                }

                reg_bmi.setText(String.format("%.2f",bmi))
                reg_bmiStatus.setText(status)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        register_weight.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val heightStr = register_height.text.toString()
                val weightStr = register_weight.text.toString()
                var status: String
                var height: String
                var weight: String
                var bmi: Double

                if (heightStr.isBlank() || heightStr.startsWith(".")) {
                    height = "0"
                } else {
                    height = heightStr
                }
                if (weightStr.isBlank() || weightStr.startsWith(".")) {
                    weight = "0"
                } else {
                    weight = weightStr
                }
                bmi = weight.toDouble() / (height.toDouble() * height.toDouble())
                if (bmi.isInfinite() || bmi.isNaN()) {
                    bmi = 0.0
                    status = ""
                } else if (bmi < 18.5) {
                    status = "(Underweight)"
                } else if (bmi >= 18.5 && bmi <= 22.9) {
                    status = "(Normal)"
                } else if (bmi >= 23.0 && bmi <= 27.4) {
                    status = "(Pre-obesity)"
                } else {
                    status = "(Obesity)"
                }

                reg_bmi.setText(String.format("%.2f", bmi))
                reg_bmiStatus.setText(status)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        btn_to_register.setOnClickListener {
            var valid : Boolean = true
            if(register_email.text.isEmpty()) {
                reg_input_layout_email.setError("Please enter your email.")
                valid = false
            }
            if (!emailRegex.matcher(register_email.text.toString()).matches())  {
                reg_input_layout_email.setError("Invalid email address")
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
            if(register_phoneNumber.text.length != 8) {
                reg_input_layout_phoneNumber.setError("Please enter 8-digit phone number.")
                valid = false
            }
            if (register_age.text.isEmpty()) {
                reg_input_layout_age.setError("Please enter your age.")
                valid = false
            }
            if (!register_age.text.isEmpty() && register_age.text.toString().toInt() >= 116) {
                reg_input_layout_age.setError("Oldest man alive is age of 116 years. Try again.")
                valid = false
            }
            if (!register_age.text.isEmpty() && register_age.text.toString().toInt() == 0) {
                reg_input_layout_age.setError("Are you kidding me? Try again.")
                valid = false
            }
            if (register_name.text.isEmpty()) {
                reg_input_layout_name.setError("Please enter your name.")
                valid = false
            }
            if (register_height.text.isEmpty() || register_height.text.toString().startsWith(".")) {
                reg_input_layout_height.setError("Please enter your height.")
                valid = false
            }
            if(!register_height.text.isEmpty() && register_height.text.toString().toDouble() >= 3.00) {
                reg_input_layout_height.setError("Tallest man on Earth is 2.72m. Try again.")
                valid = false
            }
            if(!register_height.text.isEmpty() && register_height.text.toString().toDouble() <= 0.60) {
                reg_input_layout_height.setError("Shortest man on Earth is 0.55m. Try again.")
                valid = false
            }
            if (register_weight.text.isEmpty() || register_weight.text.toString().startsWith(".")) {
                reg_input_layout_weight.setError("Please enter your weight.")
                valid = false
            }
            if(!register_weight.text.isEmpty() && register_weight.text.toString().toDouble() >= 640.00) {
                reg_input_layout_weight.setError("Heaviest man on Earth is 635kg. Try again.")
                valid = false
            }
            if(!register_weight.text.isEmpty() && register_weight.text.toString().toDouble() <= 2.10) {
                reg_input_layout_weight.setError("Lightest man on Earth is 2.1kg. Try again.")
                valid = false
            }

            if(valid) {
                if(registerUser(dailtAct, genderStr)) {
                    val myIntent = Intent(this, HomeActivity::class.java)

                    var sharedPrefEditor = sharedPreferences.edit()
                    sharedPrefEditor.putString("userPassword", register_password.text.toString())
                    sharedPrefEditor.putString("userEmail", register_email.text.toString())
                    sharedPrefEditor.putString("username",register_name.text.toString())
                    sharedPrefEditor.commit()

//                    Toast.makeText(this, "Account registered",Toast.LENGTH_SHORT).show()
                    startActivity(myIntent)
                } else {
                    Toast.makeText(this, "Account not registering",Toast.LENGTH_SHORT).show()
                }
            }

        }

        btnBack.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            this.overridePendingTransition(0, 0)
            finish()
        }

    }

    fun registerUser(dailyAct:String, genderString: String):Boolean{

        var username  = this.register_name.text.toString()
        var phoneNumber = this.register_phoneNumber.text.toString()
        var password  = this.register_password.text.toString()
        var email     = this.register_email.text.toString()
        var age       = this.register_age.text.toString()
        var height    = this.register_height.text.toString().toDouble()
        var weight    = this.register_weight.text.toString().toDouble()
        var bmi = (weight / (height*height))
        var calories: Int
        var bmr = (10 * weight) + (6.25 * height * 100) - (5 * age.toInt())
        var gender      : String = genderString
        var accountBalance: Double = "0.0".toDouble()
        var accountPoints: Int = "0".toInt()


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

        var result = kopitiamDBHelper.insertUser(UserRecord(username,gender,height,weight, bmi,age,email,accountBalance,accountPoints,carb.toInt(),calories, fat.toInt(),fibra.toInt(),minerails.toDouble(),vitamins.toDouble(),dailyAct,protein.toInt(),password,phoneNumber))
        return result
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
                if (s.length == 0 || s.toString().startsWith("."))
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
                if (s.length == 0 || s.toString().startsWith("."))
                    reg_input_layout_weight.setError("Please enter your weight.")
                else
                    reg_input_layout_weight.setError(null)
            }
        })

    }
}
