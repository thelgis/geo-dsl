package com.thelgis.geodsl.integrationtests

import org.springframework.data.jpa.repository.JpaRepository

interface PointsRepo: JpaRepository<PointEntity, Long> {
  fun findByName(name: String): PointEntity?
}