package com.example.swipeassignment.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.swipeassignment.api.ProductApi
import com.example.swipeassignment.model.ProductDataResponse
import com.example.swipeassignment.utils.Constants
import com.example.swipeassignment.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class ProductRepository @Inject constructor(val productApi: ProductApi) {

    private val _productLiveData = MutableLiveData<NetworkResult<List<ProductDataResponse>>>()
    val productLiveData:LiveData<NetworkResult<List<ProductDataResponse>>>
        get()=_productLiveData

    suspend fun getProducts(){
        _productLiveData.postValue(NetworkResult.Loading())
        val response = productApi.getProducts()

        if (response.isSuccessful && response.body() !=null){
            Log.i(Constants.TAG,response.body().toString())
            _productLiveData.postValue(NetworkResult.Success(response.body()))
        }else if (response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _productLiveData.postValue(NetworkResult.Error(null,errorObj.getString("message")))
        }else{
            _productLiveData.postValue(NetworkResult.Error(null,"Something Went Wrong"))
        }
    }



}