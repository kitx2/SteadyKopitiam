package com.example.steadykopitiam

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.activity_food_item.*
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Thread.sleep
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.Period
import java.util.*
import kotlin.collections.ArrayList

class QRActivity : AppCompatActivity() {
    lateinit var kopitiamDBHelper: DBHelper

    private var jsonURL : String? = ""
    private var finalPrice : String =" "
    private var listOrderSumm = ArrayList<OrderSummaryRecord>()
    lateinit var sharedPreferences: SharedPreferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        kopitiamDBHelper = DBHelper(this)

        val qrlabel : TextView = findViewById(R.id.qrLabel)
        val stallLabel : TextView = findViewById(R.id.stallLabel)
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sharedPreferences = getSharedPreferences("foodPriceIncPrefs", Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPreferences.edit()


        //TODO: get stall name from StallActivity

        var foodName : String = intent.getStringExtra("foodName")
        var stallName : String = intent.getStringExtra("stallName")
//        var foodPrice : String = intent.getStringExtra("foodPrice") // foodbase price by default


        // to check if food is ordered before
        var foodPrice = checkFoodOrderBefore(foodName)
        if(foodPrice.equals("")){
            foodPrice = intent.getStringExtra("foodPrice")
            sharedPrefEditor.putBoolean("isPriceIncrease",false)
        }else{
            sharedPrefEditor.putBoolean("isPriceIncrease",true)

        }
        sharedPrefEditor.commit()
        println("FoodPrice is "+foodPrice)

        qrlabel.setText("Please scan the QR code from the selected stall below.")
        stallLabel.setText(stallName)

        //TODO: Implement QR scanner
        val cameraView = findViewById<SurfaceView>(R.id.surfaceView)
        val barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(640,480)
            .build()

        cameraView.holder.addCallback(object: SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                try {
                    cameraSource.start(cameraView.holder)
                } catch (ie: IOException) {
                    Log.e("CAMERA SOURCE", ie.message)
                }
            }

            override fun surfaceChanged(
                holder: SurfaceHolder?,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource.stop()
            }

        })

        //TODO: Success in reading JSON
//        val myIntent = Intent(applicationContext, FoodItemActivity::class.java)
//        startActivity(myIntent)
//        finish()

        barcodeDetector.setProcessor((object:Detector.Processor<Barcode>{
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes = detections?.detectedItems

                if(barcodes?.size() != 0 ){

                    v.vibrate(400)
                    jsonURL = barcodes?.valueAt(0)?.displayValue
                    InternerJSON(this@QRActivity,jsonURL!!,stallName,foodName,foodPrice).execute()
                    barcodeDetector.release()
                    sleep(500)
                    finish()

//                    println(" %%%% Hellow OWrld ")

//                    //println(sharedPreferences.getString("Key", "default value"))
//                    Toast.makeText(applicationContext, sharedPreferences.getString("Key", "default value"), Toast.LENGTH_SHORT).show()
//                    cameraSource.stop()
//                    cameraSource.release()

                }
            }
        }))


    }



    private fun checkFoodOrderBefore(foodName: String ): String {
        // retrieve all order summary

        listOrderSumm = kopitiamDBHelper.retrieveAllOrderSummary()
        for(i in 0..listOrderSumm.size-1){
            if(listOrderSumm[i].orderSummaryFoodName.equals(foodName)){

                var date = listOrderSumm[i].orderSummaryTimeDate
                // convert to string to date date from order summary
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy. HH:mm:ss")
                var d = Date()
                d = simpleDateFormat.parse(date)

                val curTime = simpleDateFormat.format(Date())
                var cur = simpleDateFormat.parse(curTime)
                // means user eaten this food 24hours  before
                if((cur.time - 86400000 ) < d.time){
                    println("hahahahalololololoo")
                    return listOrderSumm[i].orderSummaryExtraPrice
                }

                println(" currtime " + cur.time)
                println("ORder sumaaary tyime "+ d.time)

            }
        }

        return ""
    }







}
