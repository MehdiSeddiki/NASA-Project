package com.example.nasaproject

import android.os.Bundle

import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.google.gson.GsonBuilder

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



/**
 * A simple [Fragment] subclass.
 * Use the [ApodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ApodFragment : Fragment(){

    lateinit var apodTitle: TextView
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
        val callback : retrofit2.Callback<ApodObject> = object : retrofit2.Callback<ApodObject> {
            override fun onResponse(
                call: retrofit2.Call<ApodObject>,
                response: Response<ApodObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("paf", "paf + " + data)
                        apodTitle = view.findViewById(R.id.apod_fragment_textView_apod_title)
                        apodTitle.setText(data.title)
                        val apodAuthor = view.findViewById(R.id.apod_fragment_textView_author) as TextView
                        if (data.copyright != null)
                            apodAuthor.setText("@" + data.copyright)
                        else
                            apodAuthor.setText("@NASA")
                        val imageView = view.findViewById(R.id.apod_fragment_imgView_potd) as ImageView
                        Glide.with(this@ApodFragment).load(data.url).into(imageView);
                        val apodDescription = view.findViewById(R.id.apod_fragment_textView_apod_description) as TextView
                        apodDescription.setText(data.explanation)


                    }
                }
                else{
                    Log.d("ApodInterface", "Servor Error" + response.code().toString())
                }
            }

            override fun onFailure(call: retrofit2.Call<ApodObject>, t: Throwable) {
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