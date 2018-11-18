package com.thelgis.geodsl.shapes

enum class Shape(
  val hibernateDescription: String
) {
  CIRCLE("circle"),
  POLYGON("polygon")
}