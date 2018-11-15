
plugins {
  `build-scan`
  `maven-publish`
  kotlin("jvm") version "1.3.10"
  id("org.jetbrains.dokka") version "0.9.17"
}

buildScan {
  // FIXME Gradle "Kotlin JVM libraries" tutorial uses deprecated methods
  setLicenseAgreementUrl("https://gradle.com/terms-of-service") 
  setLicenseAgree("yes")
  publishAlways() 
}

group = "com.thelgis.geo-dsl"
version = "0.0.1-SNAPSHOT"

val dokka by tasks.getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
  outputFormat = "html"
  outputDirectory = "$buildDir/javadoc"
}

val dokkaJar by tasks.creating(Jar::class) {
  group = JavaBasePlugin.DOCUMENTATION_GROUP
  description = "Assembles Kotlin docs with Dokka"
  classifier = "javadoc"
  from(dokka)
}

publishing {
  publications {
    create("default", MavenPublication::class.java) {
      from(components["java"])
      artifact(dokkaJar)
    }
  }
  repositories {
    maven {
      url = uri("/Users/thelgis/.m2/repository") // TODO temporary
    }
  }
}

repositories {
  jcenter() 
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  testImplementation("junit:junit:4.12")
}