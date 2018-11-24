package com.thelgis.geodsl.geometry

import com.thelgis.geodsl.DEFAULT_SRID
import com.thelgis.geodsl.geometry.shapes.Shape
import com.vividsolutions.jts.geom.Geometry

interface GeometryBuilder {
  val shape: Shape
  val geometry: Geometry

  fun create(): Geometry =
    when(shape) { // TODO No need for customisation per shape here yet, maybe delete "when" statement
      Shape.POLYGON -> geometry
      Shape.CIRCLE -> geometry
    }.also { it.srid = DEFAULT_SRID }

}