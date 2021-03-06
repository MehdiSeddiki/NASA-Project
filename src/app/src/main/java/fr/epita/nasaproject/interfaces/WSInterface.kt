package fr.epita.nasaproject.interfaces

import fr.epita.nasaproject.`object`.ApodObject
import fr.epita.nasaproject.`object`.EonetManifestObject
import fr.epita.nasaproject.`object`.EonetObject
import fr.epita.nasaproject.`object`.MrpObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface WSInterface {
    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getApodObject() : Call<ApodObject>

    @GET("planetary/apod?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo")
    fun getApodObjectLastSevenDays(@Query("start_date") startDate: String) : Call<List<ApodObject>>

    @GET
    fun getMrpObject(@Url url : String, @Query("sol") sol: Int) : Call<MrpObject>

    @GET
    fun getMrpManifests(@Url url : String) : Call<EonetManifestObject>

    @GET
    fun getEonetEvent(@Url url: String) : Call<EonetObject>
}