package com.thelgis.geodsl.geometry.shapes

import com.thelgis.geodsl.GeoDSLMarker
import com.thelgis.geodsl.geometry.GeometryBuilder
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.util.GeometricShapeFactory

@GeoDSLMarker
class CircleBuilder(
  var x: Double = 0.0,
  var y: Double = 0.0,
  var size: Double = 0.0,
  var points: Int = 0
): GeometryBuilder {
  override val shape = Shape.CIRCLE
  override lateinit var geometry: Geometry
}

fun circle(build: CircleBuilder.() -> Unit): GeometryBuilder {
  val circleBuilder = CircleBuilder().apply(build)

  circleBuilder.geometry =  GeometricShapeFactory().apply {
    setNumPoints(circleBuilder.points)
    setCentre(Coordinate(circleBuilder.x, circleBuilder.y))
    setSize(circleBuilder.size)
  }.createCircle()

  return circleBuilder

}