package com.example.nasaproject

import retrofit2.Call
import retrofit2.http.GET

interface ApodInterface {
    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getAllApodList() : Call<ApodObject>
}