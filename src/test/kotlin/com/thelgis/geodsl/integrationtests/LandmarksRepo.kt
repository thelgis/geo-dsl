package com.thelgis.geodsl.integrationtests

import org.springframework.data.jpa.repository.JpaRepository

interface LandmarksRepo: JpaRepository<Landmarks, Long> {
  fun findByName(name: String): Landmarks?
}