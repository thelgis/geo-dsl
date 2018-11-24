package com.thelgis.geodsl

import com.thelgis.geodsl.geometry.GeometryBuilder
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityManagerFactory
import kotlin.reflect.KClass

@Service
class GeoQuery @Autowired constructor(private val entityManagerFactory: EntityManagerFactory) {

  private val replacementToken = "<?>"

  fun <T: GeoEntity> run(type: KClass<T>, build: GeoQueryBuilder.() -> Unit): List<T> {

    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    val queryBuilder = GeoQueryBuilder().apply(build)

    val tableName = queryBuilder.table ?: type.simpleName
    val geometries = extractGeometries(queryBuilder.expression)
    val queryStr = replaceDollars(evaluateExpression(queryBuilder.expression))

    // language=sql
    val query = session.createQuery(
        "SELECT x FROM $tableName x WHERE $queryStr",
        type.java
    ).apply {
      // Create geometries and set each query parameter with its corresponding geometry
      geometries.forEachIndexed { index, geometryBuilder ->  setParameter("_$index", geometryBuilder.create())}
    }

    val results = query.resultList

    session.close()
    return results

  }

  private fun replaceDollars(input: String, counter: Int = 0): String =
    if (input.contains(replacementToken)) {
      replaceDollars(input.replaceFirst(replacementToken, "_$counter"), counter + 1)
    } else input


  private fun evaluateExpression(expression: GeoExpression): String =
    when(expression) {
      is SpacialExpression ->
        "${expression.whereArguments?.geoFunction?.str} (x.${expression.column}, :$replacementToken) " +
        "${expression.whereArguments?.operator?.str} ${expression.whereArguments?.operand}"
      is AND -> "${evaluateExpression(expression.left)} AND ${evaluateExpression(expression.right)}"
      is OR -> "${evaluateExpression(expression.left)} OR ${evaluateExpression(expression.right)}"
    }

  private fun extractGeometries(expression: GeoExpression, previousIterationMap: List<GeometryBuilder> = listOf()): List<GeometryBuilder> =
    when(expression) {
      is SpacialExpression -> (previousIterationMap + expression.whereArguments?.geometryBuilder) as List<GeometryBuilder>
      is AND -> extractGeometries(expression.left, extractGeometries(expression.right))
      is OR -> extractGeometries(expression.left, extractGeometries(expression.right))
    }

}

