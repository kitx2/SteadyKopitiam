package com.example.steadykopitiam

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class QRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        val qrlabel : TextView = findViewById(R.id.qrLabel)
        val stallLabel : TextView = findViewById(R.id.stallLabel)
        //TODO: get stall name from StallActivity
        var stallname = "Stall name"
        qrlabel.setText("Please scan the QR code from the selected stall below.")
        stallLabel.setText(stallname)

        //TODO: Implement QR scanner


        //TODO: Success in reading JSON
        val myIntent = Intent(applicationContext, FoodItemActivity::class.java)
        startActivity(myIntent)
        finish()
    }
}
