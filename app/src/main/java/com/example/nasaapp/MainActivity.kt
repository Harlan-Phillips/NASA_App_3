package com.example.nasaapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    private val apiKey = "DEMO_KEY" // Replace with your actual NASA API key

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.spaceButton)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val nameTextView = findViewById<TextView>(R.id.nameView)
        val dateTextView = findViewById<TextView>(R.id.dateView)

        button.setOnClickListener {
            getSpaceImage(imageView, nameTextView, dateTextView)
        }
    }

    private fun getSpaceImage(imageView: ImageView, nameTextView: TextView, dateTextView: TextView) {
        val client = AsyncHttpClient()
        val url = "https://api.nasa.gov/planetary/apod?api_key=$apiKey"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                Log.d("NASA", "API response successful")

                // Parse the JSON response
                val imageUrl = json.jsonObject.getString("url")
                val title = json.jsonObject.getString("title")
                val date = json.jsonObject.getString("date")

                // Update UI elements
                Glide.with(this@MainActivity).load(imageUrl).into(imageView)
                nameTextView.text = title
                dateTextView.text = date
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, throwable: Throwable?) {
                Log.e("NASA API Error", errorResponse ?: "Unknown error")
            }
        })
    }
}