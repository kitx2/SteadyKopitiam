package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
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

        // to log in user
        val btn_login = findViewById<Button>(R.id.btn_login)
        btn_login.setOnClickListener{
            var email = this.email_input_login.text.toString()
            var password = this.email_input_password.text.toString()

            var user = ArrayList<UserRecord>()
            user = kopitiamDBHelper.readUser(email,password)

            if(user.isEmpty()){
                Toast.makeText(this,"This User does not exists",Toast.LENGTH_LONG).show()
            }else{
                // -- to login to the main page -- //
                userpassword = user.get(0).user_password
                useremail = user.get(0).email
                var sharedPrefEditor = sharedPreferences.edit()
                sharedPrefEditor.putString("userPassword", userpassword)
                sharedPrefEditor.putString("userEmail",useremail)
                sharedPrefEditor.commit()

                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
                finish()
            }

        }


        val f1 = resources.getStringArray(R.array.food1)






    }
}


