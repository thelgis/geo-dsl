CREATE TABLE IF NOT EXISTS points (
  id BIGSERIAL PRIMARY KEY,
  name TEXT,
  location GEOMETRY
);

CREATE TABLE IF NOT EXISTS areas (
  id BIGSERIAL PRIMARY KEY,
  name TEXT,
  location GEOMETRY
);

TRUNCATE TABLE areas;
TRUNCATE TABLE points;

INSERT INTO points (name, location)
VALUES
       ('Statue of Liberty', ST_GeomFromText('POINT(-74.044508 40.689229)',4326)),
       ('Central Park Carousel', ST_GeomFromText('POINT(-73.975208 40.769806)', 4326)),
       ('Apollo Theater', ST_GeomFromText('POINT(-73.950237 40.809788)', 4326))
;

INSERT INTO areas (name, location)
VALUES
(
  'Central Park',
  ST_GeomFromText(
   'POLYGON((-73.973057 40.764356,
   -73.981898 40.768094,
   -73.958209 40.800621,
   -73.949282 40.796853,
   -73.973057 40.764356))',4326)
)
-- ,(
--   'Some circle around Statue of liberty',
--   ST_Buffer(ST_MakePoint(-74.044935, 40.691011)::geography, 300)::geometry
-- )
;

-- Testing:
-- SELECT * FROM points where st_within(ST_Buffer(ST_MakePoint(-74.044935, 40.691011)::geometry, 325))