package com.example.nasaapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    var spaceImageURL = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSpaceImageURL()
        val button = findViewById<Button>(R.id.spaceButton)
        val imageView = findViewById<ImageView>(R.id.imageView)
        getNextImage(button, imageView)
    }
    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            getSpaceImageURL()

            Glide.with(this)
                .load(spaceImageURL)
                .fitCenter()
                .into(imageView)
        }
    }
}
private fun getSpaceImageURL() {
    val client = AsyncHttpClient()

    client["https://images-api.nasa.gov/", object : JsonHttpResponseHandler() {
        override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {

            Log.d("Space", "response successful")
        }

        override fun onFailure(
            statusCode: Int,
            headers: Headers?,
            errorResponse: String,
            throwable: Throwable?
        ) {
            Log.d("Space Error", errorResponse)
        }
    }]
}

