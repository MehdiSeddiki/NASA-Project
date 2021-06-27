package com.example.nasaproject

import java.util.Date

data class ManifestObject(val sol : Int, val earth_date : Date, val total_photos: Int, val cameras : List<String>)