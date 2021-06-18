package com.example.nasaproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * A simple [Fragment] subclass.
 * Use the [MrpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MrpFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_mrp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClick(view)
    }

    public fun onClick(view: View) {
        val url = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/" // || opportunity || spirit
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(jsonConverter)
            .build()
        val service = retrofit.create(WSInterface::class.java)

        val callback : retrofit2.Callback<MrpObject> = object : retrofit2.Callback<MrpObject> {
            override fun onResponse(
                mrp: retrofit2.Call<MrpObject>,
                response: Response<MrpObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("Testing", data.toString())
                    }
                }
                else{
                    Log.d("MrpFragment Response", "Servor Error" + response.code().toString())
                }
            }

            override fun onFailure(mrp: retrofit2.Call<MrpObject>, t: Throwable) {
                Log.d("MrpFragment Failure", "WS Error " + t.message)
            }
        }

        service.getMrpPage(1000, "MAST").enqueue(callback)
    }
}