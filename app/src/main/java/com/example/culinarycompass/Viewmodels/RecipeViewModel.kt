package com.example.culinarycompass.Viewmodels

import androidx.lifecycle.ViewModel
import com.example.culinarycompass.data.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor() :ViewModel(){
    var recepi : Recipe? =null
}