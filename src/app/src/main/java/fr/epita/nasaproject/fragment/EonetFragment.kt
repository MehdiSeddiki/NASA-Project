package fr.epita.nasaproject.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaproject.R
import com.google.gson.GsonBuilder
import fr.epita.nasaproject.adapter.EonetAdapter
import fr.epita.nasaproject.`object`.EonetObject
import fr.epita.nasaproject.interfaces.WSInterface
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EonetFragment : Fragment() {
    private var category = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val t = inflater.inflate(R.layout.fragment_eonet, container, false)

        val spinnerEventType = t.findViewById<Spinner>(R.id.eonet_fragment_spinner_type)

        spinnerEventType?.adapter = activity?.applicationContext?.let {
            context?.resources?.let { it1 ->
                ArrayAdapter(
                    it,
                    R.layout.support_simple_spinner_dropdown_item,
                    it1.getStringArray(R.array.event_types)
                )
            }
        } as SpinnerAdapter
        spinnerEventType?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Eonet Event Spinner", "Nothing was selected")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                category = position + 5
                if (category >= 11)
                    category += 1
            }
        }

        super.onCreateView(inflater, container, savedInstanceState)
        return t
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Api call
        val url = "https://eonet.sci.gsfc.nasa.gov/api/v3/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(jsonConverter)
            .build()
        val service = retrofit.create(WSInterface::class.java)

        val callback : retrofit2.Callback<EonetObject> = object : retrofit2.Callback<EonetObject> {
            override fun onResponse(
                call: Call<EonetObject>,
                response: Response<EonetObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        if (data.events.isEmpty())
                            Toast.makeText(
                                activity,
                                "No data were found for this combination",
                                Toast.LENGTH_SHORT
                            ).show()
                        else {
                            val eonetRecyclerView : RecyclerView = view.findViewById(R.id.eonet_fragment_recyclerView)
                            eonetRecyclerView.setHasFixedSize(true)
                            eonetRecyclerView.adapter = EonetAdapter(data, view.context)
                            eonetRecyclerView.layoutManager = LinearLayoutManager(this@EonetFragment.context)
                        }
                    }
                } else {
                    Log.d("EonetFragment Response", "Server Error" + response.message())
                }
            }

            override fun onFailure(call: Call<EonetObject>, t: Throwable) {
                Log.d("EonetFragment Failure", "WS Error" + t.message.toString())
            }
        }

        val searchButton = view.findViewById<Button>(R.id.eonet_fragment_button_search_results)
        searchButton.setOnClickListener {
            service.getEonetEvent("categories/$category").enqueue(callback)
        }

        service.getEonetEvent("events?").enqueue(callback)
    }
}