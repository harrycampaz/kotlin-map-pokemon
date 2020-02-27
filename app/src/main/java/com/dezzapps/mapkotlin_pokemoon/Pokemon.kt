package com.dezzapps.mapkotlin_pokemoon

import android.location.Location

class Pokemon {
    var name: String? = null
    var desc: String? = null
    var power: Int? = null
    var image: Int? = null
    var isCatch: Boolean? = false
    var location: Location?=null

    constructor(name: String, desc: String, image: Int, power: Int, lat: Double, lon: Double){

        this.name = name
        this.desc = desc
        this.image = image
        this.power = power
        this.location = Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = lon
        this.isCatch = false




    }
}