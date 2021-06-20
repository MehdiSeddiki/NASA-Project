package com.example.nasaproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.iterator
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A simple [Fragment] subclass.
 * Use the [EonetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EonetFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eonet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Api call
        val url = "https://eonet.sci.gsfc.nasa.gov/api/v2.1/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(jsonConverter)
            .build()
        val service = retrofit.create(WSInterface::class.java)

        val callback : retrofit2.Callback<EonetObject> = object : retrofit2.Callback<EonetObject> {
            override fun onResponse(
                call: retrofit2.Call<EonetObject>,
                response: Response<EonetObject>
            ) {
                if (response.isSuccessful) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            Log.d("Testing", data.toString())
                        }
                    } else {
                        Log.d("EonetFragment Response", "Servor Error" + response.code().toString())
                    }
                } else {
                    Log.d("EonetFragment Response", "List: Servor Error" + response.message())
                }
            }

            override fun onFailure(call: Call<EonetObject>, t: Throwable) {
                Log.d("EonetFragment Failure", "List: WS Error" + t.message.toString())
            }
        }

        service.getEonetEvent().enqueue(callback)
    }
}