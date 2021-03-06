package com.thelgis.geodsl

import com.thelgis.geodsl.geometry.GeometryBuilder

sealed class GeoExpression

data class AND(
  val left: GeoExpression,
  val right: GeoExpression
): GeoExpression()

data class OR(
  val left: GeoExpression,
  val right: GeoExpression
): GeoExpression()

data class SpatialExpression(
  val column: String,
  val whereArguments: WhereArguments? = null
): GeoExpression()

infix fun SpatialExpression.within(geometryBuilder: GeometryBuilder) =
    this.copy(whereArguments = WhereArguments(GeoFunction.WITHIN, geometryBuilder, GeoOperator.EQ, "true"))

infix fun SpatialExpression.distanceFrom(geometryBuilder: GeometryBuilder) =
    GeoDistanceFunction(this.copy(whereArguments = WhereArguments(GeoFunction.DISTANCE, geometryBuilder)))


data class WhereArguments(
  val geoFunction: GeoFunction,
  val geometryBuilder: GeometryBuilder,
  val operator: GeoOperator? = null,
  val operand: String? = null
)

infix fun GeoExpression.and(other: GeoExpression) = AND(this, other)
infix fun GeoExpression.or(other: GeoExpression) = OR(this, other)