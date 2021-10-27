package com.example.flickrfindr

import android.os.AsyncTask
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL

// Gets JSON packets with 25 photos based on page number
class PhotoStore : AsyncTask<String, Void, String>() {
    // Returns JSON Data from GET Request
    override fun doInBackground(vararg query: String): String {
        var jsonResult = ""
        var input: String?
        try {
            val searchURL = URL(query[0])
            val httpConnection = searchURL.openConnection() as HttpURLConnection
            // SEND REQ AND CONNECT
            httpConnection.requestMethod = "GET";
            httpConnection.connect()
            val stream = InputStreamReader(httpConnection.inputStream)
            val buffReader = BufferedReader(stream as Reader?)
            val stringBuilder = StringBuilder()
            input = buffReader.readLine()
            while (input != null) {
                stringBuilder.append(input)
                input = buffReader.readLine()
            }
            buffReader.close()
            stream.close()
            jsonResult = stringBuilder.toString()
        } catch (e: IOException) {
            print("ERROR");
            e.printStackTrace()
        }
        return jsonResult
    }
}