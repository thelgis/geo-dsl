package com.thelgis.geodsl

data class GeoDistanceFunction(val spacialExpression: SpacialExpression)

infix fun GeoDistanceFunction.lessThan(distance: Double) =
    spacialExpression.copy(
        whereArguments = spacialExpression.whereArguments?.copy(
            operator = GeoOperator.LT,
            operand = distance.toString()
        )
    )

infix fun GeoDistanceFunction.lessThanOrEq(distance: Double) =
    spacialExpression.copy(
        whereArguments = spacialExpression.whereArguments?.copy(
            operator = GeoOperator.LTE,
            operand = distance.toString())
    )

infix fun GeoDistanceFunction.moreThan(distance: Double) =
    spacialExpression.copy(
        whereArguments = spacialExpression.whereArguments?.copy(
            operator = GeoOperator.MR,
            operand = distance.toString())
    )

infix fun GeoDistanceFunction.moreThanOrEq(distance: Double) =
    spacialExpression.copy(
        whereArguments = spacialExpression.whereArguments?.copy(
            operator = GeoOperator.MRE,
            operand = distance.toString())
    )