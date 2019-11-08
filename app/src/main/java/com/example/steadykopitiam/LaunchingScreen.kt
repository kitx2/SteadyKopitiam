package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.steadykopitiam.ui.home.HomeActivity
import java.util.*

class LaunchingScreen : AppCompatActivity() {

    var user = ArrayList<UserRecord>()
    lateinit var kopitiamDBHelper: DBHelper

    lateinit var sharedPreferences : SharedPreferences
    private var userpassword : String? = ""
    private var useremail : String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launching_screen)

        kopitiamDBHelper = DBHelper(this)
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        Handler().postDelayed({
            kopitiamDBHelper = DBHelper(this)

            userpassword = sharedPreferences.getString("userPassword", "")
            useremail = sharedPreferences.getString("userEmail", "")
            user = kopitiamDBHelper.readUser(useremail!!,userpassword!!)

            if(!user.isNullOrEmpty()) {
                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
                finish()
            } else {
                val myIntent = Intent(this, LoginActivity::class.java)
                startActivity(myIntent)
                finish()
            }
        },3000)



    }
}
