package com.example.culinarycompass.Viewmodels

import android.annotation.SuppressLint
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.culinarycompass.data.Hit
import com.example.culinarycompass.data.QueryParams
import com.example.culinarycompass.data.SearchResult
import com.example.culinarycompass.data.SearchResultPagingSource
import com.example.culinarycompass.data.SearchResultRepository
import com.example.culinarycompass.parser.ApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchResultRepository: SearchResultRepository
):ViewModel() {
 /*  private val _recepies = MutableLiveData<MutableList<Hit>>()
    val recepies: LiveData<MutableList<Hit>> = _recepies*/

    private val _recipiresponse: MutableStateFlow<PagingData<Hit>> =
        MutableStateFlow(PagingData.empty())
    var recepiResponse = _recipiresponse.asStateFlow()
        private set

    var searchtext by mutableStateOf("pizza")
    var showBottomSheet by mutableStateOf(false)

@SuppressLint("SuspiciousIndentation")
fun getrecipes()
{

    viewModelScope.launch {
        val queryParams = QueryParams(search = searchtext, diet = arrayOf(), health = arrayOf(),cusine = arrayOf("Asian"),mealtype = arrayOf(),dishtype = arrayOf(), nextpage = "")
        val pagingSource = SearchResultPagingSource(searchResultRepository, queryParams)
        val pagingData = Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = { pagingSource }
        ).flow
            .cachedIn(viewModelScope)
            .collect { pagingData ->
                _recipiresponse.value = pagingData
            }

    }
}

}


/* viewModelScope.launch(Dispatchers.IO) {
        try {
        val apicall :Call<SearchResult> = ApiClient().apiInterface.getRecipes(search = searchtext, diet = arrayOf(), health = arrayOf(), cusine = arrayOf(), mealtype = arrayOf("dinner"), dishtype = arrayOf())
            println(apicall)
            apicall.enqueue(object :Callback<SearchResult>{
                override fun onResponse(p0: Call<SearchResult>, p1: Response<SearchResult>) {
                    if (_recepies.value == null)
                    {
                        _recepies.value = mutableListOf()
                    }
                    var list = _recepies.value
                    list?.addAll(p1.body()?.hits ?: listOf())
                   _recepies.value = list?.toMutableList()
                }

                override fun onFailure(p0: Call<SearchResult>, p1: Throwable) {
                    println(p1.message)
                }
            })
        }catch (e :Exception){
            println(e)
        }
    }*/