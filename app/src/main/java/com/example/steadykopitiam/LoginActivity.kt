package com.example.steadykopitiam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.steadykopitiam.ui.home.HomeActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {


    lateinit var kopitiamDBHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // init database
        kopitiamDBHelper = DBHelper(this)

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
            println("Before read User ")
            user = kopitiamDBHelper.readUser(email,password)
//            user = kopitiamDBHelper.readAllUser()

//            var str: String? = null
//            str = "Fetched " + user.size + " user : \n"
//            user.forEach {
//                str = str + it.email.toString() + " - " + it.user_password.toString() + " " + it.age + "\n"
//            }
//
//            Toast.makeText(this,"User retireved "+ str,Toast.LENGTH_LONG).show()

//            git test

            if(user.isEmpty()){
                Toast.makeText(this,"This User does not exists",Toast.LENGTH_LONG).show()
            }else{
                // -- to login to the main page -- //
                println("%%%%babababaab")
                val myIntent = Intent(this, HomeActivity::class.java)
                startActivity(myIntent)
                finish()
            }

        }


        val f1 = resources.getStringArray(R.array.food1)






    }
}


