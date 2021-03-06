package com.thelgis.geodsl.integrationtests

import com.thelgis.geodsl.GeoEntity
import com.vividsolutions.jts.geom.Point
import javax.persistence.*

@Entity
@Table(name = "points")
data class Landmarks(
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
  val name: String,
  val location: Point
): GeoEntity