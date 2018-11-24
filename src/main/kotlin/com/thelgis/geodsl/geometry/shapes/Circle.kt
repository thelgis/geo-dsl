package com.thelgis.geodsl.geometry.shapes

import com.thelgis.geodsl.GeoDSLMarker
import com.thelgis.geodsl.geometry.GeometryBuilder
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.util.GeometricShapeFactory

@GeoDSLMarker
class CircleBuilder(
  var points: Int = 0,
  var size: Double = 0.0,
  var centerX: Double = 0.0,
  var centerY: Double = 0.0
): GeometryBuilder {
  override val shape = Shape.CIRCLE
  override lateinit var geometry: Geometry
}

fun circle(build: CircleBuilder.() -> Unit): GeometryBuilder {
  val circleBuilder = CircleBuilder().apply(build)

  circleBuilder.geometry =  GeometricShapeFactory().apply {
    setNumPoints(circleBuilder.points)
    setCentre(Coordinate(circleBuilder.centerX, circleBuilder.centerY))
    setSize(circleBuilder.size)
  }.createCircle()

  return circleBuilder

}