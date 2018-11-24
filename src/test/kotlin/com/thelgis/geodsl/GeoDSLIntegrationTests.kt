package com.thelgis.geodsl

import com.thelgis.geodsl.entities.PointEntity
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

//  @Autowired
//  private lateinit var pointsRepo: PointsRepo

  @Autowired
  private lateinit var entityManagerFactory: EntityManagerFactory

  @Autowired
  private lateinit var geoQuery: GeoQuery

  @Test
  fun contextLoads() {}

  @Test
  fun `Hibernate | find points in polygon`() {

    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    val polygon =
        """
          |POLYGON(
          |(-73.973057 40.764356,
          |-73.981898 40.768094,
          |-73.958209 40.800621,
          |-73.949282 40.796853,
          |-73.973057 40.764356)
          )
        """.trimMargin()

    val wktReader = WKTReader()
    val polygonGeometry = wktReader.read(polygon)
    polygonGeometry.srid = DEFAULT_SRID

    // Run query
    val query = session.createQuery(
        "select p from PointEntity p where within(p.location, :polygon) = true",
        PointEntity::class.java
    )
    query.setParameter("polygon", polygonGeometry)

    Assert.assertTrue(query.resultList.map { it.name }.contains("Central Park Carousel"))
    Assert.assertFalse(query.resultList.map { it.name }.contains("Apollo Theater"))

    session.close()

  }

  @Test
  fun `DSL | find points in polygon`() {

    val results =
        geoQuery.run(PointEntity::class) {
          where {
            col("location") within polygon(
                LatLon(-73.973057, 40.764356),
                LatLon(-73.981898, 40.768094),
                LatLon(-73.958209, 40.800621),
                LatLon(-73.949282, 40.796853),
                LatLon(-73.973057, 40.764356)
            )
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertFalse(results.map { it.name }.contains("Apollo Theater"))
  }

  @Test
  fun `Hibernate | find distance from polygon`() {

    val session = entityManagerFactory.unwrap(SessionFactory::class.java).openSession()

    val polygon =
        """
          |POLYGON(
          |(-73.973057 40.764356,
          |-73.981898 40.768094,
          |-73.958209 40.800621,
          |-73.949282 40.796853,
          |-73.973057 40.764356)
          )
        """.trimMargin()

    val wktReader = WKTReader()
    val polygonGeometry = wktReader.read(polygon)
    polygonGeometry.srid = DEFAULT_SRID

    // Run query
    val query = session.createQuery(
        "select p from PointEntity p where distance(p.location, :polygon) <= 0.1",
        PointEntity::class.java
    )
    query.setParameter("polygon", polygonGeometry)

    query.resultList.forEach { println("RESULT: " + it) }

    session.close()

  }

  @Test
  fun `DSL | find distance from polygon`() {

    val results =
        geoQuery.run(PointEntity::class) {
          where {
            col("location") distanceFrom polygon(
                LatLon(-73.973057, 40.764356),
                LatLon(-73.981898, 40.768094),
                LatLon(-73.958209, 40.800621),
                LatLon(-73.949282, 40.796853),
                LatLon(-73.973057, 40.764356)
            ) lessThanOrEq  0.1
          }
        }

    // TODO assertions
    results.forEach { println("RESULT: " + it) }
  }

  @Test
  fun `DSL | find points in polygon and distance from polygon`() {

    val results =
        geoQuery.run(PointEntity::class) {
          where {
            col("location") within polygon(
                LatLon(-73.973057, 40.764356),
                LatLon(-73.981898, 40.768094),
                LatLon(-73.958209, 40.800621),
                LatLon(-73.949282, 40.796853),
                LatLon(-73.973057, 40.764356)
            ) or (col("location") distanceFrom polygon(
                LatLon(-73.973057, 40.764356),
                LatLon(-73.981898, 40.768094),
                LatLon(-73.958209, 40.800621),
                LatLon(-73.949282, 40.796853),
                LatLon(-73.973057, 40.764356)
            ) moreThan 0.1)
          }
        }


    results.forEach { println("RESULT: " + it) }
    // TODO assertions

  }

  @Test
  fun `DSL | find points in circle`() {

    val results =
        geoQuery.run(PointEntity::class) {
          where {
            col("location") within circle {
              points = 32
              size = 325.0
              centerX = 40.691011
              centerY = -74.044935
            }
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Statue of Liberty"))
  }

}