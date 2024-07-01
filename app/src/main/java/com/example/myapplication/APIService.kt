package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET

interface APIService {
    @GET("path to endpoint")
    fun getText(): Call<ServerResponse>
}

// Define the data class for the server response
data class ServerResponse(val text: String)
