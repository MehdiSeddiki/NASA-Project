package fr.epita.nasaproject.`object`

import java.util.Date

data class PhotoManifestObject(val name : String,
                               val landing_date : Date,
                               val launch_date : Date,
                               val status : String,
                               val max_sol : Int,
                               val max_date : Date,
                               val total_photos: Int,
                               var photos : List<ManifestObject>)