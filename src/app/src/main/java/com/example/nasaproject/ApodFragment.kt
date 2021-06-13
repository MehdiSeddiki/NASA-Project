package com.example.nasaproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


/**
 * A simple [Fragment] subclass.
 * Use the [ApodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApodFragment : Fragment(){

    private val apodViewModel by activityViewModels<MainActivity.ApodViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_apod, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Api call
        val url = "https://api.nasa.gov/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(jsonConverter)
            .build()
        val service = retrofit.create(ApodInterface::class.java)
        val callback : Callback<ApodObject> = object : Callback<ApodObject> {
            override fun onResponse(
                call: Call<ApodObject>,
                response: Response<ApodObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("paf", "paf + " + data)
                    }
                }
                else{
                    Log.d("ApodInterface", "Servor Error" + response.code().toString())
                }
            }

            override fun onFailure(call: Call<ApodObject>, t: Throwable) {
                Log.d("ApodInterface", "WS Error " + t.message)
            }
        }
        service.getAllApodList().enqueue(callback)
    }

    fun observePicList() {
        apodViewModel.picList.observe(
            viewLifecycleOwner, { newValue -> println(newValue) }
        )
    }
}