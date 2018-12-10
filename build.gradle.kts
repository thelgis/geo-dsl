import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.11"
val springBootVersion = "2.1.0.RELEASE"
val hibernateSpatialVersion = "5.3.7.Final"
val junitVersion = "4.12"
val jvmTargetVersion = "1.8"

plugins {
  val kotlinVersion = "1.3.11"
  val dokkaVersion = "0.9.17"

  `build-scan`
  `maven-publish`
  kotlin("jvm") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
  id("org.jetbrains.dokka") version dokkaVersion
}

//buildScan {
//  // FIXME Gradle "Kotlin JVM libraries" tutorial uses deprecated methods
//  setLicenseAgreementUrl("https://gradle.com/terms-of-service")
//  setLicenseAgree("yes")
//  publishAlways()
//}

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

// Customise Tasks
val compileKotlin by tasks.getting(KotlinCompile::class) {
  kotlinOptions.jvmTarget = jvmTargetVersion
}

val compileTestKotlin by tasks.getting(KotlinCompile::class) {
  kotlinOptions.jvmTarget = jvmTargetVersion
}

tasks.withType<Test> {
  if (properties["test.profile"] != "integration") {
    exclude("com/thelgis/geodsl/integrationtests/**")
  }
}

repositories {
  jcenter()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.hibernate:hibernate-spatial:$hibernateSpatialVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")

  testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
  testImplementation("junit:junit:$junitVersion")
}