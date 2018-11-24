package com.thelgis.geodsl

enum class GeoOperator(
  val str: String
) {
  EQ("="),
  LT("<"),
  LTE("<="),
  MR(">"),
  MRE(">=")
}