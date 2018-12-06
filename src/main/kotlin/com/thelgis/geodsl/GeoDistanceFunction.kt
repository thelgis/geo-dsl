package com.thelgis.geodsl

data class GeoDistanceFunction(val spatialExpression: SpatialExpression)

infix fun GeoDistanceFunction.lessThan(distance: Double) =
    spatialExpression.copy(
        whereArguments = spatialExpression.whereArguments?.copy(
            operator = GeoOperator.LT,
            operand = distance.toString()
        )
    )

infix fun GeoDistanceFunction.lessThanOrEq(distance: Double) =
    spatialExpression.copy(
        whereArguments = spatialExpression.whereArguments?.copy(
            operator = GeoOperator.LTE,
            operand = distance.toString())
    )

infix fun GeoDistanceFunction.moreThan(distance: Double) =
    spatialExpression.copy(
        whereArguments = spatialExpression.whereArguments?.copy(
            operator = GeoOperator.MR,
            operand = distance.toString())
    )

infix fun GeoDistanceFunction.moreThanOrEq(distance: Double) =
    spatialExpression.copy(
        whereArguments = spatialExpression.whereArguments?.copy(
            operator = GeoOperator.MRE,
            operand = distance.toString())
    )