package com.thelgis.geodsl.repos

import com.thelgis.geodsl.entities.PointEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PointsRepo: JpaRepository<PointEntity, Long> {
  fun findByName(name: String): PointEntity?
}