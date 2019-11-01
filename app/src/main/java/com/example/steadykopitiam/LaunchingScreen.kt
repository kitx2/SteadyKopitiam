package com.example.steadykopitiam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import java.util.*

class LaunchingScreen : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launching_screen)

        Handler().postDelayed({

            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        },3000)



    }
}
