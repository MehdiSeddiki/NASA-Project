package com.example.nasaproject

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.acl.Group
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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



    @RequiresApi(Build.VERSION_CODES.O)
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
        val callback2 : retrofit2.Callback<List<ApodObject>> = object : retrofit2.Callback<List<ApodObject>> {
            override fun onResponse(
                call: retrofit2.Call<List<ApodObject>>,
                response: Response<List<ApodObject>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        Log.d("paf2", "paf2 + " + data)
                        val ids = view.findViewById<LinearLayout>(R.id.linearLayout)
                        var tmp = 0
                        for (i in ids) {
                            val imgView = view.findViewById<View>(i.id) as ImageView
                            Glide.with(this@ApodFragment).load(data[tmp].url).into(imgView);
                            tmp++
                        }
                    }
                }
                else {
                    Log.d("response", response.message())
                    Log.d("failed", "failed to fetch data")
                }
            }

            override fun onFailure(call: Call<List<ApodObject>>, t: Throwable) {
                Log.d("failure", t.message.toString())
            }
        }
        service.getAllApodList().enqueue(callback)
        service.getLast7Apod(getDaysAgo(7)).enqueue(callback2)
    }

    fun observePicList() {
        apodViewModel.picList.observe(
            viewLifecycleOwner, { newValue -> println(newValue) }
        )
    }


    fun getDaysAgo(daysAgo: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date = Date()
        System.out.println("Current Date " + sdf.format(date))

        // Convert Date to Calendar
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, -8)

         date = c.getTime()

        val format1 = SimpleDateFormat("yyyy-MM-dd")

        var inActiveDate: String? = null

        try {
            inActiveDate = format1.format(date)
            println(inActiveDate)
            return inActiveDate
        } catch (e1: ParseException) {

            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        return date.time.toString()
    }
}