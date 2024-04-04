package com.example.nasaapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

// Data class to hold NASA Image data
data class NasaImage(val imageUrl: String, val title: String, val date: String)

// Adapter class for RecyclerView
class NasaImageAdapter(private val images: List<NasaImage>) : RecyclerView.Adapter<NasaImageAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val titleView: TextView = view.findViewById(R.id.titleView)
        val dateView: TextView = view.findViewById(R.id.dateView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nasa_image, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = images[position]
        Glide.with(holder.imageView.context).load(item.imageUrl).into(holder.imageView)
        holder.titleView.text = item.title
        holder.dateView.text = item.date
    }

    override fun getItemCount() = images.size
}

// Main activity class
class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var spaceButton: Button
    private val apiKey = "DEMO_KEY" // Replace with your actual NASA API key
    private val images = mutableListOf<NasaImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = NasaImageAdapter(images)
        }

        spaceButton = findViewById<Button>(R.id.spaceButton)
        spaceButton.setOnClickListener {
            fetchSpaceImages()
        }
    }

    private fun fetchSpaceImages() {
        val client = AsyncHttpClient()
        val url = "https://api.nasa.gov/planetary/apod?api_key=$apiKey&count=10"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                val jsonArray = json.jsonArray

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val imageUrl = jsonObject.getString("url")
                    val title = jsonObject.getString("title")
                    val date = jsonObject.getString("date")
                    val image = NasaImage(imageUrl, title, date)
                    images.add(image)
                }

                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onFailure(statusCode: Int, headers: Headers?, errorResponse: String?, throwable: Throwable?) {
                // Handle failure
            }
        })
    }
}