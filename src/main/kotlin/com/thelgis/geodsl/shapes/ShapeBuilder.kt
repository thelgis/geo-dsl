package com.thelgis.geodsl.shapes

import com.thelgis.geodsl.DEFAULT_SRID
import com.vividsolutions.jts.geom.Geometry

interface ShapeBuilder {
  val shape: Shape
  val geometry: Geometry

  fun create(): Geometry =
    when(shape) { // No need for customisation per shape here yet, maybe delete "when" statement
      Shape.CIRCLE -> { geometry }
      Shape.POLYGON -> { geometry }
    }.also { it.srid = DEFAULT_SRID }

}