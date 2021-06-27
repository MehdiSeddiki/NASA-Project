package fr.epita.nasaproject.`object`

data class EventObject(val id : String,
                       val title: String,
                       val description : String,
                       val link : String,
                       val categories : List<CategoryObject>,
                       val sources : List<SourceObject>,
                       val geometry : List<GeometryObject>)