package com.thelgis.geodsl

import com.thelgis.geodsl.shapes.ShapeBuilder
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.persistence.EntityManagerFactory
import kotlin.reflect.KClass

@Service
class GeoQuery @Autowired constructor(private val entityManagerFactory: EntityManagerFactory) {

  fun <T: GeoEntity> run(type: KClass<T>, build: QueryBuilder.() -> Unit): List<T> {
    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    val queryBuilder = QueryBuilder().apply(build)

    val tableName = queryBuilder.table ?: type.simpleName
    val whereArguments = queryBuilder.whereArguments
    val polygonBuilder = whereArguments.shapeBuilder
    val hibernateDescription = polygonBuilder.shape.hibernateDescription

    // language=sql
    val query = session.createQuery(
        """
          |SELECT x
          |FROM $tableName x
          |WHERE ${whereArguments.geoFunction}(x.${queryBuilder.column}, :$hibernateDescription) = true
        """.trimMargin(),
        type.java
    ).apply {
      setParameter(hibernateDescription, whereArguments.shapeBuilder.create())
    }

    val results = query.resultList

    session.close()
    return results
  }


}

@GeoDSLMarker
class QueryBuilder {

  var table: String? = null
  lateinit var column: String
  lateinit var whereArguments: WhereArguments

  fun from(table: String) {
    this.table = table
  }

  fun where(build: SpacialFunctions.(spacialFunctions: SpacialFunctions) -> SpacialFunctions) {
    val emptySpacialFunctions = SpacialFunctions()
    val spacialFunctions = emptySpacialFunctions.build(emptySpacialFunctions)

    this.column = spacialFunctions.column ?: throw IllegalArgumentException("Where context must be provided with a 'column'")
    this.whereArguments = spacialFunctions.whereArguments ?: throw IllegalArgumentException("Where context must be provided with 'WhereArguments'")
  }

}

@GeoDSLMarker
data class SpacialFunctions(
  var whereArguments: WhereArguments? = null,
  var column: String? = null
) {

  operator fun get(newColumn: String) = this.copy(column = newColumn)

  infix fun within(shapeBuilder: ShapeBuilder) =
      this.copy(whereArguments = WhereArguments("within", shapeBuilder))

}

data class WhereArguments(
  val geoFunction: String,
  val shapeBuilder: ShapeBuilder
)