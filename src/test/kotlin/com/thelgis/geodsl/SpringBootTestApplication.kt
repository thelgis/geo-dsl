package com.thelgis.geodsl

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.thelgis.geodsl"])
class SpringBootTestApplication

fun main(args: Array<String>) {
  runApplication<SpringBootTestApplication>(*args)
}