package com.example.nasaproject

import java.util.*

data class PhotoObject(val id : Int, val sol : Int, val camera : CameraObject, val img_src : String, val earth_date : Date, val rover : RoverObject)