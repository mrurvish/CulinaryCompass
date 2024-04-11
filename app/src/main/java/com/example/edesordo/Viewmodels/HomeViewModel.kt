package com.example.edesordo.Viewmodels

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edesordo.data.Hit
import com.example.edesordo.data.Recipe
import com.example.edesordo.data.SearchResult
import com.example.edesordo.parser.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class HomeViewModel:ViewModel() {
   private val _recepies = MutableLiveData<MutableList<Hit>>()
    val recepies: MutableLiveData<MutableList<Hit>> = _recepies


@SuppressLint("SuspiciousIndentation")
fun getrecipes(s: String)
{
    viewModelScope.launch(Dispatchers.IO) {
        try {
        val apicall :Call<SearchResult> = ApiClient().apiInterface.getRecipes(search = s, diet = arrayOf(), health = arrayOf(), cusine = arrayOf(), mealtype = arrayOf("dinner"), dishtype = arrayOf())
            println(apicall)
            apicall.enqueue(object :Callback<SearchResult>{
                override fun onResponse(p0: Call<SearchResult>, p1: Response<SearchResult>) {
                    if (_recepies.value == null)
                    {
                        _recepies.value = mutableListOf()
                    }
                    _recepies.value?.addAll(p1.body()?.hits ?: listOf())
                }

                override fun onFailure(p0: Call<SearchResult>, p1: Throwable) {
                    println("error")
                }
            })
        }catch (e :Exception){
            println(e)
        }
    }
}
}