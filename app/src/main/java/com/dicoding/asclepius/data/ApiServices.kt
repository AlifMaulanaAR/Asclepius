package com.dicoding.asclepius.data

import com.dicoding.asclepius.data.response.Responses
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("https://newsapi.org/v2/top-headlines?country=id&category=health&apiKey=7f284c0dbe484d05a9055d9c567917ec")
    fun getUsers(
        @Query("q=") users: String
    ): Call<Responses>
}