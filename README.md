```
 _____                 ______  _____ _     
|  __ \                |  _  \/  ___| |    
| |  \/ ___  ___ ______| | | |\ `--.| |    
| | __ / _ \/ _ \______| | | | `--. \ |    
| |_\ \  __/ (_) |     | |/ / /\__/ / |____
 \____/\___|\___/      |___/  \____/\_____/                             
```


Geo-DSL is a small library that demonstrates a limited Kotlin Domain Specific Language
for Hibernate driven geo-spatial queries. It can be used on top of a a Spring Application 
that utilizes Hibernate as its ORM. 

Currently the library is not meant to be used in a real production application. It is 
developed as a proof of concept of wrapping Hibernate geo-spatial queries in a DSL. 

The basic parts covered by the DSL is building geography shapes and running geo-spatial queries. 

For example, creating a `Circle` on the map can be expressed as: 

```kotlin
circle {
  x = 40.691011
  y = -74.044935
  size = 325.0
  points = 32
}
```

Another example: getting all points from the database table that are either inside
Central Park, or less than some distance from it: 

```kotlin
val centralPark = polygon {
  + LatLon(-73.973057, 40.764356)
  + LatLon(-73.981898, 40.768094)
  + LatLon(-73.958209, 40.800621)
  + LatLon(-73.949282, 40.796853)
  + LatLon(-73.973057, 40.764356)
}

val results: List<PointEntity> = 
  geoQuery.run(PointEntity::class) {
    where {
      col("location") within centralPark or
      (col("location") distanceFrom centralPark lessThan 0.1)
    }
  }
```

The above query will detect the table that is related through Hibernate to the 
`PointEntity` class and collect all points that their `location` column is fulfilling the 
specific `where` clause. The points will be mapped to `PointEntity` instances and will 
be returned in a list. 

You can optionally declare the Entity class that defines the database table inside the 
`from` function. But, it is not needed since this can be implied by the class mapping.  

```kotlin 
geoQuery.run(PointEntity::class) {
  from("PointEntity")
  where {
    ...
  }
}
```

Every `run() {...}` call starts and terminates a Hibernate Session. Instantiating the 
GeoQuery class can either happen through normal construction by providing an entity manager
factory 

```kotlin
val geoQuery = GeoQuery(entityManagerFactory)
```

or by Spring dependency injection 

```kotlin
@Autowired lateinit var geoQuery: GeoQuery
```

## Example Integration Tests

The library contains Integration tests tha use a small embedded SpringBoot application 
to demonstrate how to write queries. All examples are in the `GeoDSLIntegrationTests` class
and contain query examples with the DSL and their non-DSL pure Hibernate counter part. 

To run the integration tests you will need a Postgres Database with the PostGIS plugin. 
You can easily set up the integration tests Database with Docker (you will need to have Docker installed). 

```bash
docker volume create postgis_data
docker run --name=postgis -d -e POSTGRES_USER=test -e POSTGRES_PASS=test -e POSTGRES_DBNAME=postgis -e ALLOW_IP_RANGE=0.0.0.0/0 -p 5432:5432 -v postgis_data:/var/lib/postgresql kartoza/postgis:9.6-2.4
``` 

Then create the database schema and populate it with data by running the following script:
```bash
geo-dsl/src/test/resources/db/integration_tests_db.sql
```

Run the tests by: 
```bash
./gradlew test
```
or use the `.bat` file if you are using Windows.

## Use the library in your Spring application 

TODO 

## Features & Contributing 

As explained above the initial purpose of this repository is not to create a complete DSL 
for geo-spatial queries, but to demonstrate how such a DSL would look like. Nevertheless, 
feel free to contribute to enhance the demonstrated functionality. If the project ever reaches a very mature state 
it could be officially released.  
 
Currently supported features: 

* Creating Geography objects 
  * Polygon 
  * Circle 
* Querying 
  * `within` queries
  * `distance` queries
  * chaining geo queries with `and`/`or` 
  

  
