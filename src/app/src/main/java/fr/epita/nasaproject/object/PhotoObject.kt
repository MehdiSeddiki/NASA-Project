package fr.epita.nasaproject.`object`

import java.util.Date

data class PhotoObject(val id : Int,
                       val sol : Int,
                       val camera : CameraObject,
                       val img_src : String,
                       val earth_date : Date,
                       val rover : RoverObject
)