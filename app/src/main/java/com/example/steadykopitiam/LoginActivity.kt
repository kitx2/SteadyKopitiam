package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.steadykopitiam.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header_main.*

class LoginActivity : AppCompatActivity() {


    lateinit var kopitiamDBHelper: DBHelper

    lateinit var sharedPreferences : SharedPreferences
    private var userpassword : String = ""
    private var useremail : String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // init database
        kopitiamDBHelper = DBHelper(this)
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        // to sign up user
        val btn_register = findViewById<Button>(R.id.btn_signup)
        btn_register.setOnClickListener{
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)
        }

        // error message
        val errorCard : CardView = this.findViewById(R.id.errorCard)
        errorCard.visibility = View.INVISIBLE
        val errorMsg : TextView = findViewById(R.id.errorMsg)

        // to log in user
        val btn_login = findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener{
            val email = this.email_input_login.text.toString()
            val password = this.email_input_password.text.toString()

            if(email_input_login.text.isEmpty() || email_input_password.text.isEmpty()) {
                errorCard.visibility = View.VISIBLE
                errorMsg.text = "Please enter the required field(s)"
            } else {

                var user = ArrayList<UserRecord>()
                user = kopitiamDBHelper.readUser(email, password)

                if (user.isEmpty()) {
                    errorCard.visibility = View.VISIBLE
                    errorMsg.text = "Invalid email or password."
                } else {
                    // -- to login to the main page -- //
                    errorCard.visibility = View.INVISIBLE
                    userpassword = user.get(0).user_password
                    useremail = user.get(0).email
                    var sharedPrefEditor = sharedPreferences.edit()
                    sharedPrefEditor.putString("userPassword", userpassword)
                    sharedPrefEditor.putString("userEmail", useremail)
                    sharedPrefEditor.commit()

                    val myIntent = Intent(this, HomeActivity::class.java)
                    startActivity(myIntent)
                    finish()
                }
            }

        }

        val f1 = resources.getStringArray(R.array.food1)

    }
}


