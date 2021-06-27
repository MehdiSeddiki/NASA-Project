package fr.epita.nasaproject.`object`

import java.util.Date

data class RoverObject(val id : Int,
                       val name : String,
                       val landing_date : Date,
                       val launch_date: Date,
                       val status : String)