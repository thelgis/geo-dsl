package com.thelgis.geodsl

/**
 * The query builder holds a table and an expression that will be run on the table.
 * If the table is omitted then conventions will be used to make an assumption about
 * where the expression should run.
 */
@GeoDSLMarker
class GeoQueryBuilder {

  var table: String? = null
  lateinit var expression: GeoExpression

  fun from(table: String) {
    this.table = table
  }

  fun where(build: WhereBuilder.() -> GeoExpression) {
    this.expression = WhereBuilder().build()
  }

}

@GeoDSLMarker class WhereBuilder {
  fun col(column: String) = SpacialExpression(column)
}