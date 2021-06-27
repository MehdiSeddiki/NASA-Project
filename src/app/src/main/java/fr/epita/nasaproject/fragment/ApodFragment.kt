package fr.epita.nasaproject.fragment

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
import com.bumptech.glide.Glide
import com.example.nasaproject.R
import com.google.gson.GsonBuilder
import fr.epita.nasaproject.`object`.ApodObject
import fr.epita.nasaproject.interfaces.WSInterface
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ApodFragment : Fragment(){
    lateinit var apodTitle: TextView
    private var dataApod = ApodObject("", "", "", "")
    private var dataListApod = ArrayList<ApodObject>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_apod, container, false)
    }

    @ExperimentalStdlibApi
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
        val service = retrofit.create(WSInterface::class.java)

        val callbackApodObject : retrofit2.Callback<ApodObject> = object : retrofit2.Callback<ApodObject> {
            override fun onResponse(
                call: Call<ApodObject>,
                response: Response<ApodObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        dataApod = data
                        apodTitle = view.findViewById(R.id.apod_fragment_textView_apod_title)
                        apodTitle.text = data.title
                        val apodAuthor = view.findViewById(R.id.apod_fragment_textView_author) as TextView
                        if (data.copyright != null)
                            apodAuthor.text = "@${data.copyright}"
                        else
                            apodAuthor.text = "@NASA"
                        val imageView = view.findViewById(R.id.apod_fragment_imgView_potd) as ImageView
                        Glide.with(this@ApodFragment).load(data.url).into(imageView)
                        val apodDescription = view.findViewById(R.id.apod_fragment_textView_apod_description) as TextView
                        apodDescription.text = data.explanation
                    }
                }
                else{
                    Log.d("ApodFragment Response", "Object: Server Error" + response.code().toString())
                }
            }

            override fun onFailure(call: Call<ApodObject>, t: Throwable) {
                Log.d("ApodFragment Failure", "Object: WS Error " + t.message)
            }
        }

        val callbackApodList : retrofit2.Callback<List<ApodObject>> = object : retrofit2.Callback<List<ApodObject>> {
            override fun onResponse(
                call: Call<List<ApodObject>>,
                response: Response<List<ApodObject>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        dataListApod = data as ArrayList<ApodObject>
                        val ids = view.findViewById<LinearLayout>(R.id.linearLayout)
                        var cnt = 0
                        for (i in ids) {
                            val imgView = view.findViewById<View>(i.id) as ImageView
                            Glide.with(this@ApodFragment).load(data[cnt].url).into(imgView)
                            imgView.setOnClickListener{
                                val index = i.resources.getResourceName(i.id)[i.resources.getResourceName(i.id).length - 1].digitToInt()

                                val tmpApod = dataApod
                                dataApod = dataListApod[index]
                                dataListApod[index] = tmpApod

                                val apodAuthor = view.findViewById(R.id.apod_fragment_textView_author) as TextView
                                if (dataApod.copyright != null)
                                    apodAuthor.text = "@${dataApod.copyright}"
                                else
                                    apodAuthor.text = "@NASA"

                                apodTitle.text = dataApod.title

                                val apodDescription = view.findViewById(R.id.apod_fragment_textView_apod_description) as TextView
                                apodDescription.text = dataApod.explanation

                                Glide.with(this@ApodFragment).load(dataListApod[index].url).into(imgView)
                                Glide.with(this@ApodFragment).load(dataApod.url).into(view.findViewById(
                                    R.id.apod_fragment_imgView_potd
                                ) as ImageView)
                            }
                            cnt++
                        }
                    }
                }
                else {
                    Log.d("ApodFragment Response", "List: Server Error" + response.message())
                }
            }

            override fun onFailure(call: Call<List<ApodObject>>, t: Throwable) {
                Log.d("ApodFragment Failure", "List: WS Error" + t.message.toString())
            }
        }

        service.getAllApodList().enqueue(callbackApodObject)
        service.getLast7Apod(getDaysAgo()).enqueue(callbackApodList)
    }

    private fun getDaysAgo(): String {
        var date = Date()

        // Convert Date to Calendar
        val c = Calendar.getInstance()
        c.time = date
        c.add(Calendar.DATE, -7)

        date = c.time

        val format1 = SimpleDateFormat("yyyy-MM-dd")

        try {
            return format1.format(date)
        } catch (e1: ParseException) {
            e1.printStackTrace()
        }
        return date.time.toString()
    }
}