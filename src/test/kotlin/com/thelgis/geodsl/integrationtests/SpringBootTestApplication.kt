package com.thelgis.geodsl.integrationtests

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.thelgis.geodsl"])
class SpringBootTestApplication

fun main(args: Array<String>) {
  runApplication<SpringBootTestApplication>(*args)
}