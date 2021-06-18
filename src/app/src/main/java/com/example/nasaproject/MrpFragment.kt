package com.example.nasaproject

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
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
    private var rover = "curiosity";
    private var camera = "";

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        //return inflater.inflate(R.layout.fragment_mrp, container, false)
        val t = inflater.inflate(R.layout.fragment_mrp, container, false)
        val spinnerRover = t.findViewById<Spinner>(R.id.mrp_fragment_spinner_rover)
        val spinnerCamera = t.findViewById<Spinner>(R.id.mrp_fragment_spinner_camera)
        spinnerRover?.adapter = activity?.applicationContext?.let {
            context?.getResources()?.let { it1 ->
                ArrayAdapter(
                    it,
                    R.layout.support_simple_spinner_dropdown_item,
                    it1.getStringArray(R.array.rovers)
                )
            }
        } as SpinnerAdapter
        spinnerRover?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                rover = (parent!!.getChildAt(0) as TextView).text.toString()
                (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                println(type)
            }

        }

        spinnerCamera?.adapter = activity?.applicationContext?.let {
            context?.getResources()?.let { it1 ->
                ArrayAdapter(
                    it,
                    R.layout.support_simple_spinner_dropdown_item,
                    it1.getStringArray(R.array.cameras)
                )
            }
        } as SpinnerAdapter
        spinnerCamera?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                println("erreur")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val type = parent?.getItemAtPosition(position).toString()
                camera = (parent!!.getChildAt(0) as TextView).text.toString()
                (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                Toast.makeText(activity,type, Toast.LENGTH_LONG).show()
                println(type)
            }

        }

        val button = t.findViewById<Button>(R.id.mrp_fragment_button_search_results)
        button.setOnClickListener {
            var extension = rover
            if (extension == "Default")
                extension = "curiosity"
            val url = "https://api.nasa.gov/mars-photos/api/v1/rovers/$extension/"
            val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(jsonConverter)
                .build()
            val service = retrofit.create(WSInterface::class.java)

            val callback: retrofit2.Callback<MrpObject> = object : retrofit2.Callback<MrpObject> {
                override fun onResponse(
                    mrp: retrofit2.Call<MrpObject>,
                    response: Response<MrpObject>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            Log.d("Testing", data.toString())
                        }
                    } else {
                        Log.d("MrpFragment Response", "Servor Error" + response.code().toString())
                    }
                }

                override fun onFailure(mrp: retrofit2.Call<MrpObject>, t: Throwable) {
                    Log.d("MrpFragment Failure", "WS Error " + t.message)
                }
            }
            if (camera == "Default")
                service.getMrpPage(1000).enqueue(callback)
            else
                service.getMrpPageWithCam(1000, camera).enqueue(callback)
        }

        super.onCreateView(inflater, container, savedInstanceState)
        return t
    }
}