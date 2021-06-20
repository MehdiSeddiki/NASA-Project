package com.example.nasaproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WSInterface {
    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getAllApodList() : Call<ApodObject>

    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getLast7Apod(@Query("start_date") startDate: String) : Call<List<ApodObject>>

    @GET("photos?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getMrpPageWithCam(@Query("sol") sol: Int, @Query("camera") Camera: String) : Call<MrpObject>

    @GET("photos?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getMrpPage(@Query("sol") sol: Int) : Call<MrpObject>

    @GET("events?")
    fun getEonetEvent() : Call<EonetObject>

    @GET
    fun getEonetEventFilteredByCategory(@Url url: String) : Call<EonetObject>
}