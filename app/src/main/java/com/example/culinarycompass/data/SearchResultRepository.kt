package com.example.culinarycompass.data

import com.example.culinarycompass.parser.ApiClient
import javax.inject.Inject


class SearchResultRepository @Inject constructor(private val apiclient: ApiClient) {
    suspend fun getrecipies(
        search: String,
        diet: Array<String>?,
        health: Array<String>?,
        cusine: Array<String>?,
        mealtype: Array<String>?,
        dishtype: Array<String>?,
        nextpage:String?
    ):SearchResult = apiclient.apiInterface.getRecipes(search = search,diet = diet, health = health, cusine = cusine, mealtype = mealtype, dishtype = dishtype, nextpage = nextpage)
}