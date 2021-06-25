package com.example.nasaproject

data class EonetObject(val title : String,
                       val description : String,
                       val link : String,
                       val events : List<EventObject>)