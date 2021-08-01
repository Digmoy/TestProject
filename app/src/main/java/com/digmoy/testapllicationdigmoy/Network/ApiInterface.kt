package com.digmoy.testapllicationdigmoy.Network

import com.digmoy.testapllicationdigmoy.ApiModel.Photos
import retrofit2.http.GET

interface ApiInterface {

    @GET("photos")
    suspend fun getPhotos () : List<Photos>
}