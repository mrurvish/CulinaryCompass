package com.example.culinarycompass.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchResultPagingSource @Inject constructor(private val searchResultRepository: SearchResultRepository,private val queryParams: QueryParams) :
    PagingSource<String, Hit>() {
    override fun getRefreshKey(state: PagingState<String, Hit>): String? {
      return null
    }

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Hit> {
       val page: String? = params.key?:null
        val response = searchResultRepository.getrecipies(search = queryParams.search,diet = queryParams.diet,health = queryParams.health, cusine = queryParams.cusine,mealtype = queryParams.mealtype,dishtype = queryParams.dishtype,page)
        return try {
            LoadResult.Page(
                data = response.hits,
                prevKey = null,
                nextKey =getContValue(response.links.next.href)
            )
        } catch (e: IOException) {
            LoadResult.Error(
                e
            )
        } catch (e: HttpException) {
            LoadResult.Error(
                e
            )
        }    }
    fun getContValue(url: String): String? {
        val regex = Regex("[?&]_cont=([^&]+)")
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value
    }
}