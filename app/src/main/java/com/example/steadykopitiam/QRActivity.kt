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

class QRActivity : AppCompatActivity() {
    var allPermissionsGrantedFlag : Int = 0
    private var jsonURL : String? = ""

    lateinit var sharedPreferences: SharedPreferences


    private val permissionList = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        val qrlabel : TextView = findViewById(R.id.qrLabel)
        val stallLabel : TextView = findViewById(R.id.stallLabel)
        val v = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sharedPreferences = this.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPreferences.edit()


        //TODO: get stall name from StallActivity

        var foodName : String = intent.getStringExtra("foodName")
        var stallName : String = intent.getStringExtra("stallName")
        var foodPrice : String = intent.getStringExtra("foodPrice")

        println("FoodName is QQQQQWWWWWW "+ foodName)

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
//                    finish()
//
                    println(" %%%% Hellow OWrld ")
                    sleep(5)
                    //println(sharedPreferences.getString("Key", "default value"))
                    Toast.makeText(applicationContext, sharedPreferences.getString("Key", "default value"), Toast.LENGTH_SHORT).show()
                    cameraSource.stop()
                    cameraSource.release()

                }
            }
        }))

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(allPermissionEnabled()){
                allPermissionsGrantedFlag = 1
            }else{
                setupMultipePermissions()
            }
        }else{
            allPermissionsGrantedFlag = 1
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun allPermissionEnabled() : Boolean {
        return permissionList.none{checkSelfPermission(it)!=
                PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupMultipePermissions(){
        val remaniningPermission = permissionList.filter{
            checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
        }
        requestPermissions(remaniningPermission.toTypedArray(),101)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 101 ){
            if(grantResults.any{it != PackageManager.PERMISSION_GRANTED}){
                @TargetApi(Build.VERSION_CODES.M)
                if(permissionList.any{shouldShowRequestPermissionRationale(it)}){
                    AlertDialog.Builder(this)
                        .setMessage("Press Permission to Decide Permission Again ")
                        .setPositiveButton("Permission"){dialog,which -> setupMultipePermissions()}
                        .setNegativeButton("Cancel"){dialog, which -> dialog.dismiss()}
                        .create()
                        .show()
                }
            }
        }
    }



}
