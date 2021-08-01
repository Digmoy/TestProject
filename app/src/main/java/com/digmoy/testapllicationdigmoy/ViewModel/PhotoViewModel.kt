package com.digmoy.testapllicationdigmoy.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digmoy.testapllicationdigmoy.ApiModel.Photos
import com.digmoy.testapllicationdigmoy.Network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {

    private var photoLiveDate : MutableLiveData<List<Photos>> = MutableLiveData()

    fun getPhoto() : MutableLiveData<List<Photos>>{
        return photoLiveDate
    }

    fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            val retroInstance = RetrofitClient.apiInterface.getPhotos()
            photoLiveDate.postValue(retroInstance)
        }
    }


}