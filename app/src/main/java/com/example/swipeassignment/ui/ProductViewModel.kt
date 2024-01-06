package com.example.swipeassignment.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.swipeassignment.model.ProductDataResponse
import com.example.swipeassignment.repository.ProductRepository
import com.example.swipeassignment.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(private val repository: ProductRepository):ViewModel() {

    val productLiveData:LiveData<NetworkResult<List<ProductDataResponse>>>
        get() = repository.productLiveData

    fun getProducts(){
        viewModelScope.launch {
            repository.getProducts()
        }

    }

}