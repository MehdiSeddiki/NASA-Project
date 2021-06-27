package fr.epita.nasaproject.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasaproject.R
import com.google.gson.GsonBuilder
import fr.epita.nasaproject.*
import fr.epita.nasaproject.`object`.EonetManifestObject
import fr.epita.nasaproject.`object`.MrpObject
import fr.epita.nasaproject.`object`.PhotoObject
import fr.epita.nasaproject.adapter.MrpAdapter
import fr.epita.nasaproject.interfaces.WSInterface
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MrpFragment : Fragment() {
    private var rover = "curiosity"
    private var camera = ""
    private var oldData = ArrayList<PhotoObject>()

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val t = inflater.inflate(R.layout.fragment_mrp, container, false)

        val spinnerRover = t.findViewById<Spinner>(R.id.mrp_fragment_spinner_rover)
        val spinnerCamera = t.findViewById<Spinner>(R.id.mrp_fragment_spinner_camera)
        spinnerRover?.adapter = activity?.applicationContext?.let {
            context?.resources?.let { it1 ->
                ArrayAdapter(
                    it,
                    R.layout.support_simple_spinner_dropdown_item,
                    it1.getStringArray(R.array.rovers)
                )
            }
        } as SpinnerAdapter
        spinnerRover?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Mrp Rover Spinner", "Nothing was selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rover = (parent!!.getChildAt(0) as TextView).text.toString()
                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
            }

        }

        spinnerCamera?.adapter = activity?.applicationContext?.let {
            context?.resources?.let { it1 ->
                ArrayAdapter(
                    it,
                    R.layout.support_simple_spinner_dropdown_item,
                    it1.getStringArray(R.array.cameras)
                )
            }
        } as SpinnerAdapter
        spinnerCamera?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Mrp Camera Spinner", "Nothing was selected")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                camera = (parent!!.getChildAt(0) as TextView).text.toString()
                (parent.getChildAt(0) as TextView).setTextColor(Color.WHITE)
            }

        }
        val url = "https://api.nasa.gov/mars-photos/api/v1/"
        val jsonConverter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(jsonConverter)
            .build()
        val service = retrofit.create(WSInterface::class.java)

        val callbackDraw: retrofit2.Callback<MrpObject> = object : retrofit2.Callback<MrpObject> {
            override fun onResponse(
                mrp: retrofit2.Call<MrpObject>,
                response: Response<MrpObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        val mrpRecyclerView =
                            view?.findViewById<RecyclerView>(R.id.mrp_fragment_recyclerView)
                        mrpRecyclerView?.setHasFixedSize(true)
                        if ((data.photos.size + oldData.size) < 25)
                        {
                            for (photo in data.photos)
                                oldData.add(photo)
                            service.getMrpPage(
                                "rovers/curiosity/photos?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo&page=1",
                                1000
                            ).enqueue(this)
                        }
                        else
                        {
                            var i = 0
                            while (oldData.size < 25) {
                                oldData.add(data.photos[i])
                                ++i
                            }
                            data.photos = oldData
                            mrpRecyclerView?.adapter = MrpAdapter(data, view?.context as Context)
                            mrpRecyclerView?.layoutManager =
                                LinearLayoutManager(this@MrpFragment.context)
                            oldData = ArrayList()
                        }
                    }
                }
            }

            override fun onFailure(mrp: retrofit2.Call<MrpObject>, t: Throwable) {
                Log.d("MrpFragment Failure", "WS Error " + t.message)
            }
        }

        val callbackManifest: retrofit2.Callback<EonetManifestObject> = object : retrofit2.Callback<EonetManifestObject> {
            override fun onResponse(
                mrp: retrofit2.Call<EonetManifestObject>,
                response: Response<EonetManifestObject>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        data.photo_manifest.photos.asReversed()

                        var extension = rover
                        if (extension == "Default")
                            extension = "curiosity"

                        if (camera != "Default")
                            data.photo_manifest.photos = data.photo_manifest.photos.filter { manifestObject -> manifestObject.cameras.contains(camera) }
                        data.photo_manifest.photos = data.photo_manifest.photos.filter { manifestObject -> manifestObject.total_photos > 25 }

                        if (data.photo_manifest.photos.isEmpty()) {
                            Toast.makeText(
                                activity,
                                "No photos were found for this combination",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        if (camera == "Default")
                            service.getMrpPage(
                                "rovers/${extension.toLowerCase(Locale.ROOT)}/photos?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo&page=1",
                                data.photo_manifest.photos[0].sol
                            ).enqueue(callbackDraw)
                        else
                            service.getMrpPageWithCam(
                                "rovers/${extension.toLowerCase(Locale.ROOT)}/photos?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo&page=1",
                                data.photo_manifest.photos[0].sol,
                                camera
                            ).enqueue(callbackDraw)
                    }
                }
            }

            override fun onFailure(mrp: retrofit2.Call<EonetManifestObject>, t: Throwable) {
                Log.d("MrpFragment Failure", "WS Error " + t.message)
            }
        }

        val searchButton = t.findViewById<Button>(R.id.mrp_fragment_button_search_results)
        searchButton.setOnClickListener {
            var extension = rover
            if (extension == "Default")
                extension = "curiosity"

            service.getMrpManifests(
                "manifests/${extension.toLowerCase(Locale.ROOT)}?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo"
            ).enqueue(callbackManifest)
        }

        service.getMrpPage(
            "rovers/curiosity/photos?api_key=mwrnIqB77s6ArbAYaFKPE8a6ngU7Q5T9NHTbIvfo&page=1",
            1000
        ).enqueue(callbackDraw)

        super.onCreateView(inflater, container, savedInstanceState)
        return t
    }
}