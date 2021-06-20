package com.example.nasaproject

import android.icu.text.CaseMap

data class EventObject(val id : String,
                       val title: String,
                       val description : String,
                       val link : String,
                       val categories : List<CategoryObject>,
                       val sources : List<SourceObject>,
                       val geometries : List<GeometryObject>)