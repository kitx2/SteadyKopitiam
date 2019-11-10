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
import android.view.MenuItem
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.steadykopitiam.ui.about.AboutActivity
import com.example.steadykopitiam.ui.home.HomeActivity
import com.example.steadykopitiam.ui.profile.ProfileActivity
import com.example.steadykopitiam.ui.purchases.PurchasesActivity
import com.example.steadykopitiam.ui.wallet.WalletActivity
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_food_item.*
import kotlinx.android.synthetic.main.activity_food_item.drawerLayout
import kotlinx.android.synthetic.main.activity_food_item.navigationView
import kotlinx.android.synthetic.main.activity_food_item.toolbar
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
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
    var navigationPosition: Int = 0
    private var jsonURL : String? = ""
    private var finalPrice : String =" "
    private var listOrderSumm = ArrayList<OrderSummaryRecord>()
    lateinit var sharedPreferences: SharedPreferences
    var username : String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        kopitiamDBHelper = DBHelper(this)
        setTitle("Stall QR Scanner")

        val preferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        username = preferences.getString("username","")
        initView()

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
                    sleep(800)
                    finish()

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

                    return listOrderSumm[i].orderSummaryExtraPrice
                }
            }
        }

        return ""
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navigationView : NavigationView = findViewById(R.id.navigationView)

        setSupportActionBar(toolbar)
        setUpDrawerLayout()

        //Load Inbox fragment first
        navigationPosition = R.id.nav_home
        navigationView.setCheckedItem(navigationPosition)
        toolbar.title = "Home"

        navigationView.setNavigationItemSelectedListener  { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    toolbar.title = "Home"
                    navigationPosition = R.id.nav_home
                    val myIntent = Intent(this, HomeActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_purchases -> {
                    toolbar.title = "Purchases"
                    navigationPosition = R.id.nav_purchases
                    val myIntent = Intent(this, PurchasesActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_profile -> {
                    toolbar.title = "Profile"
                    navigationPosition = R.id.nav_profile
                    val myIntent = Intent(this, ProfileActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_wallet -> {
                    toolbar.title = "Wallet"
                    navigationPosition = R.id.nav_wallet
                    val myIntent = Intent(this, WalletActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
                R.id.nav_about -> {
                    toolbar.title = "About"
                    navigationPosition = R.id.nav_about
                    val myIntent = Intent(this, AboutActivity::class.java)
                    startActivity(myIntent)
                    this.overridePendingTransition(0, 0)
                    finish()
                }
            }
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            drawerLayout.closeDrawers()
            true
        }

        //Change navigation header information
        changeNavigationHeaderInfo()

    }

    private fun changeNavigationHeaderInfo() {
        val headerView = navigationView.getHeaderView(0)
        headerView.username.text = username
    }

    private fun setUpDrawerLayout() {
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onBackPressed() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        if (navigationPosition == R.id.nav_home) {
            finish()
        } else {
            //Navigate to Inbox Fragment
            navigationPosition = R.id.nav_home
            navigationView.setCheckedItem(navigationPosition)
            toolbar.title = "Home"
        }
    }

}
