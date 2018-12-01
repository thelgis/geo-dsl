package com.thelgis.geodsl.integrationtests

import com.thelgis.geodsl.*
import com.thelgis.geodsl.geometry.LatLon
import com.thelgis.geodsl.geometry.shapes.circle
import com.thelgis.geodsl.geometry.shapes.polygon
import com.vividsolutions.jts.io.WKTReader
import org.hibernate.SessionFactory
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.persistence.EntityManagerFactory

@RunWith(SpringRunner::class)
@SpringBootTest
class GeoDSLIntegrationTests {

  @Autowired
  private lateinit var entityManagerFactory: EntityManagerFactory

  @Autowired
  private lateinit var geoQuery: GeoQuery

  @Test
  fun contextLoads() {}

  @Test
  fun `Hibernate | find points in polygon`() {

    val centralParkStr =
        """
          |POLYGON((
          |-73.973057 40.764356,
          |-73.981898 40.768094,
          |-73.958209 40.800621,
          |-73.949282 40.796853,
          |-73.973057 40.764356))
        """.trimMargin()

    val wktReader = WKTReader()
    val centralPark = wktReader.read(centralParkStr)
    centralPark.srid = DEFAULT_SRID

    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    val query = session.createQuery(
        "select p from Landmarks p where within(p.location, :polygon) = true",
        Landmarks::class.java
    )
    query.setParameter("polygon", centralPark)

    val results = query.resultList
    session.close()

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertFalse(results.map { it.name }.contains("Apollo Theater"))

  }

  @Test
  fun `DSL | find points in polygon`() {

    val results =
        geoQuery.run(Landmarks::class) {
          where {
            col("location") within polygon {
              + LatLon(-73.973057, 40.764356)
              + LatLon(-73.981898, 40.768094)
              + LatLon(-73.958209, 40.800621)
              + LatLon(-73.949282, 40.796853)
              + LatLon(-73.973057, 40.764356)
            }
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertFalse(results.map { it.name }.contains("Apollo Theater"))

  }

  @Test
  fun `Hibernate | find distance from polygon`() {

    val centralParkStr =
        """
          |POLYGON((
          |-73.973057 40.764356,
          |-73.981898 40.768094,
          |-73.958209 40.800621,
          |-73.949282 40.796853,
          |-73.973057 40.764356))
        """.trimMargin()

    val wktReader = WKTReader()
    val centralPark = wktReader.read(centralParkStr)
    centralPark.srid = DEFAULT_SRID

    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    val query = session.createQuery(
        "select p from Landmarks p where distance(p.location, :polygon) <= 0.1",
        Landmarks::class.java
    )
    query.setParameter("polygon", centralPark)

    val results = query.resultList
    session.close()

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertTrue(results.map { it.name }.contains("Apollo Theater"))
    Assert.assertFalse(results.map { it.name }.contains("Statue of Liberty"))

  }

  @Test
  fun `DSL | find distance from polygon`() {

    val results =
        geoQuery.run(Landmarks::class) {
          where {
            col("location") distanceFrom polygon {
              + LatLon(-73.973057, 40.764356)
              + LatLon(-73.981898, 40.768094)
              + LatLon(-73.958209, 40.800621)
              + LatLon(-73.949282, 40.796853)
              + LatLon(-73.973057, 40.764356)
            } lessThanOrEq  0.1
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertTrue(results.map { it.name }.contains("Apollo Theater"))
    Assert.assertFalse(results.map { it.name }.contains("Statue of Liberty"))

  }

  @Test
  fun `Hibernate | find points in polygon and distance from polygon`() {

    val centralParkStr =
        """
          |POLYGON((
          |-73.973057 40.764356,
          |-73.981898 40.768094,
          |-73.958209 40.800621,
          |-73.949282 40.796853,
          |-73.973057 40.764356))
        """.trimMargin()

    val wktReader = WKTReader()
    val centralPark = wktReader.read(centralParkStr)
    centralPark.srid = DEFAULT_SRID

    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    // language=sql
    val query = session.createQuery(
        """
          | select p from Landmarks p where within(p.location, :polygon) = true or
          | distance(p.location, :polygon) > 0.1
        """.trimMargin(),
        Landmarks::class.java
    )
    query.setParameter("polygon", centralPark)

    val results = query.resultList
    session.close()

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertTrue(results.map { it.name }.contains("Statue of Liberty"))

  }

  @Test
  fun `DSL | find points in polygon and distance from polygon`() {

    val centralPark = polygon {
      + LatLon(-73.973057, 40.764356)
      + LatLon(-73.981898, 40.768094)
      + LatLon(-73.958209, 40.800621)
      + LatLon(-73.949282, 40.796853)
      + LatLon(-73.973057, 40.764356)
    }

    val results =
        geoQuery.run(Landmarks::class) {
          where {
            col("location") within centralPark or
              (col("location") distanceFrom centralPark moreThan 0.1)
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertTrue(results.map { it.name }.contains("Statue of Liberty"))

  }

  @Test
  fun `DSL | find points in circle`() {

    val results =
        geoQuery.run(Landmarks::class) {
          where {
            col("location") within circle {
              x = 40.691011
              y = -74.044935
              size = 325.0
              points = 32
            }
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Statue of Liberty"))

  }

}