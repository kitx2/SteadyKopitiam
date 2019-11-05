package com.example.steadykopitiam

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.widget.TextView
import android.widget.Toast
import com.example.steadykopitiam.ui.home.HomeActivity
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class InternerJSON (private var c: Context, private var jsonURL: String,private var foodStall : String, private var foodName : String, private var foodPrice: String ) : AsyncTask<Void, Void, String>(){
    //val sharefPref = c?.getSharedPreferences()
    lateinit  var sharedPref : SharedPreferences
    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Void?): String {
        return download()
    }

    override fun onPostExecute(jsonData: String?) {
        super.onPostExecute(jsonData)
        sharedPref = c?.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
        var sharedPrefEditor = sharedPref.edit()
        sharedPrefEditor.putString("SomeString", "SomeValue")
        sharedPrefEditor.commit()
        //Toast.makeText(c, "commited!", Toast.LENGTH_SHORT).show()


        if (jsonData!!.startsWith("URL ERROR")) {
            val error = jsonData
            Toast.makeText(c, error, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "Connect problem most probably due to wrong URL",
                Toast.LENGTH_LONG).show()
        } else if (jsonData.startsWith("CONNECT ERROR")) {
            println("Hello World ")
            val error = jsonData
            Toast.makeText(c, error, Toast.LENGTH_LONG).show()
            Toast.makeText(c, "Connect problem most probably cannot connect to any network",
                Toast.LENGTH_LONG).show()
        } else {
//            Toast.makeText(c, "Network Connection and Download successful, now parsing ...",
//                Toast.LENGTH_LONG).show()
            JsonParser(c, jsonData,foodName,foodStall,foodPrice).execute()
            // @jsonparser:
            // 1. parse ur data
            // 2. store at sharedpreferece
            // 2.1: quentin (GSON)
            // 2.2: convert the object to string and store as string
            // 2.2.1: retrieve the string convert to object
//              val myIntent = Intent(c,HomeActivity::class.java)
//                myIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//              c.startActivity(myIntent)
        }

    }

    private fun connect(jsonURL: String): Any {
        try {
            val url = URL(jsonURL) // note: import from java.net.URL

            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true
            return con
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return "URL ERROR " + e.message
        } catch (e: IOException) {
            e.printStackTrace()
            return "CONNECTION ERROR " + e.message
        }
    }

    private fun download():String {
        val connection = connect(jsonURL)
        if (connection.toString().startsWith("Error")) {
            return connection.toString()
        }
        try {
            val con = connection as HttpURLConnection
            if (con.responseCode == 200) {
                val bis = BufferedInputStream(con.inputStream)
                val br = BufferedReader(InputStreamReader(bis))
                val jsonData = StringBuffer()
                var line: String?

                do {
                    line = br.readLine()
                    if (line == null) {
                        break
                    }
                    jsonData.append(line + "\n")

                } while (true)
                br.close()
                bis.close()
                return jsonData.toString()
            } else {
                return "Error " + con.responseMessage
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return "Error " + e.message
        }
    }

}