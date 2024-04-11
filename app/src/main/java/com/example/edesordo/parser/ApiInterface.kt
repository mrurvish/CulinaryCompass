package com.example.edesordo.parser

import com.example.edesordo.data.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
const val app_id = "413f57b7"
const val app_key = "ff4cfc17a37ac2b468f050fa08e3995b"
interface ApiInterface {

    @GET("/api/recipes/v2")
    fun getRecipes(
        @Query("app_id") appid :String = app_id,
        @Query("app_key") appkey :String = app_key,
        @Query("type") type :String = "any",
        @Query("q") search:String,
        @Query("diet") diet: Array<String>?,
        @Query("health") health: Array<String>?,
        @Query("cuisineType") cusine: Array<String>?,
        @Query("mealType") mealtype: Array<String>?,
        @Query("dishType") dishtype: Array<String>?,): Call<SearchResult>
}