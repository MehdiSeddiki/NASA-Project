package com.example.nasaproject

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.time.format.DateTimeFormatter
import java.util.*

interface ApodInterface {
    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getAllApodList() : Call<ApodObject>

    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getLast7Apod(@Query("start_date") startDate: String) : Call<List<ApodObject>>
}