package com.thelgis.geodsl

import com.thelgis.geodsl.entities.PointEntity
import com.thelgis.geodsl.repos.PointsRepo
import com.thelgis.geodsl.shapes.LatLong
import com.thelgis.geodsl.shapes.circle
import com.thelgis.geodsl.shapes.polygon
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
class GeoDSLTests {

  @Autowired
  private lateinit var pointsRepo: PointsRepo

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
            it["location"] within polygon (
                LatLong(-73.973057, 40.764356),
                LatLong(-73.981898, 40.768094),
                LatLong(-73.958209, 40.800621),
                LatLong(-73.949282, 40.796853),
                LatLong(-73.973057, 40.764356)
            )
          }
        }

    Assert.assertTrue(results.map { it.name }.contains("Central Park Carousel"))
    Assert.assertFalse(results.map { it.name }.contains("Apollo Theater"))
  }

  @Test
  fun `DSL | find points in circle`() {

    val results =
        geoQuery.run(PointEntity::class) {
          where {
            it["location"] within circle {
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