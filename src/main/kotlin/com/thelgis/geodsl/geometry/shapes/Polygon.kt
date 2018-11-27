package com.thelgis.geodsl.geometry.shapes

import com.thelgis.geodsl.GeoDSLMarker
import com.thelgis.geodsl.geometry.GeometryBuilder
import com.thelgis.geodsl.geometry.LatLonBuilder
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.io.WKTReader

@GeoDSLMarker
class PolygonBuilder(
  override val geometry: Geometry
): GeometryBuilder {
  override val shape = Shape.POLYGON
}

fun polygon(build: LatLonBuilder.() -> Unit): GeometryBuilder {
  val latlons = LatLonBuilder(ArrayList()).apply(build).latlons

  return PolygonBuilder(
      WKTReader().read(
          """
            |POLYGON((
            |${latlons.joinToString { "${it.lat} ${it.lon}" }}
            |))
          """.trimMargin()
      )
  )

}


