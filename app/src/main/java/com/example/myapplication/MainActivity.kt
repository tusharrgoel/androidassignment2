package com.example.myapplication

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var welcomeTextView: TextView
    private lateinit var fetchRetroFit : Button
    private lateinit var fetchLocal : Button
    private lateinit var fetchTextView : TextView

    private val sharedPrefFile = "com.example.myapplication.PREFERENCE_FILE_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        welcomeTextView = findViewById(R.id.welcomeTextView)
        fetchRetroFit = findViewById(R.id.fetchFromRetroFit)
        fetchLocal = findViewById(R.id.fetchFromLocal)
        fetchTextView = findViewById((R.id.fetchedTextView))


        val sharedPref = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val storedText = sharedPref.getString("text", "Default Welcome Text")
        fetchTextView.text = storedText


        fetchRetroFit.setOnClickListener {
            fetchTextFromServer()
        }
        fetchLocal.setOnClickListener {
            val storedText = sharedPref.getString("text", "Default SharedPrefs Text")
            fetchTextView.text = storedText
        }

    }

    private fun fetchTextFromServer() {
        val retrofit = Retrofit.Builder().baseUrl("https://api.io/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val apiService = retrofit.create(APIService::class.java)
        apiService.getText().enqueue(object : Callback<ServerResponse> {
            override fun onResponse(
                call: Call<ServerResponse>,
                response: Response<ServerResponse>
            ) {
                if (response.isSuccessful) {
                    val serverText = response.body()?.text ?: "Default Server Text"
                    fetchTextView.text = serverText

                    // Store text in local storage
                    val sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("text", serverText)
                    editor.apply()
                }
            }

            override fun onFailure(call: Call<ServerResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }
}

