package com.thelgis.geodsl.geometry.shapes

import com.thelgis.geodsl.GeoDSLMarker
import com.thelgis.geodsl.geometry.GeometryBuilder
import com.thelgis.geodsl.geometry.LatLon
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.io.WKTReader

@GeoDSLMarker
class PolygonBuilder(
  override val geometry: Geometry
): GeometryBuilder {
  override val shape = Shape.POLYGON
}

fun polygon(vararg latlons: LatLon): GeometryBuilder =
    PolygonBuilder(
        WKTReader().read(
            """
          |POLYGON((
          |${latlons.joinToString { "${it.lat} ${it.lon}" }}
          |))
        """.trimMargin()
        )
    )

