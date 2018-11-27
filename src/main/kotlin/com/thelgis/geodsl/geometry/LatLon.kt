package com.thelgis.geodsl.geometry

data class LatLon(
  val lat: Double,
  val lon: Double
)

class LatLonBuilder(val latlons: ArrayList<LatLon>) {

  operator fun LatLon.unaryPlus() {
    latlons.add(this)
  }

}
