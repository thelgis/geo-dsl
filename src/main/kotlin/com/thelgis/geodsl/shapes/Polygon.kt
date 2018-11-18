package com.thelgis.geodsl.shapes

import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.io.WKTReader

class PolygonBuilder(
  var points: List<LatLong> = listOf()
): ShapeBuilder {
  override val shape: Shape = Shape.POLYGON
  override lateinit var geometry: Geometry
}

object polygon {

  operator fun invoke(vararg latlongs: LatLong): ShapeBuilder {
    val polygonBuilder = PolygonBuilder(latlongs.toList())

    val polygonStr =
        """
          |POLYGON((
          |${polygonBuilder.points.map { "${it.lat} ${it.long}" }.joinToString()}
          |))
        """.trimMargin()

    polygonBuilder.geometry = WKTReader().read(polygonStr)

    return polygonBuilder
  }

}