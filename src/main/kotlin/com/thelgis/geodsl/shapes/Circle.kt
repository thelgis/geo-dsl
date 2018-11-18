package com.thelgis.geodsl.shapes

import com.thelgis.geodsl.GeoDSLMarker
import com.vividsolutions.jts.geom.Coordinate
import com.vividsolutions.jts.geom.Geometry
import com.vividsolutions.jts.util.GeometricShapeFactory

@GeoDSLMarker
class CircleBuilder(
  var points: Int = 0,
  var size: Double = 0.0,
  var centerX: Double = 0.0,
  var centerY: Double = 0.0
): ShapeBuilder {
  override val shape: Shape = Shape.CIRCLE
  override lateinit var geometry: Geometry
}

fun circle(build: CircleBuilder.() -> Unit): ShapeBuilder {
  val circleBuilder = CircleBuilder().apply(build)

  circleBuilder.geometry =  GeometricShapeFactory().apply {
    setNumPoints(circleBuilder.points)
    setCentre(Coordinate(circleBuilder.centerX, circleBuilder.centerY))
    setSize(circleBuilder.size)
  }.createCircle()

  return circleBuilder

}