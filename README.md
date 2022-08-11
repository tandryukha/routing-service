# Getting Started

REST API that takes as input two IATA airport codes and provides as output a shortest route between these two airports

Amount of legs/flights is restricted per route (that is, 3(or different numbe according to configuration) stops/layovers, if going from A->B, a valid route could be A->1->2->3->B, or for example A->1->B etc.)
Service allows changing airports during stops that are within 100km(can be changed in config file) of each other. For example, if going from A->B, a valid route could be A->1->2=>3->4->B, where “2=>3” is a change of airports done via ground. These switches are not considered as part of the legs/layover/hop count, but their distance should be reflected in the final distance calculated for the route.

# Running
- Make sure Docker desktop is installed & running
- Run `docker-compose up`
- Test application is healthy by navigating to [URL]( http://localhost:8080/?airport1=TLL&airport2=TAY)
- Navigate to [Open API documentation](http://localhost:8080/swagger-ui/#/) and play with an API

# Running without docker cache (during development)
1. Stop and remove existing containers `docker-compose down --rmi all`
2. Start and infrastructure locally in docker compose `docker-compose up --force-recreate`

# API
GET http://localhost:8080/?airport1=TLL&airport2=TAY
Response: TLL=>HEL->TAY (344.77 km)

# Customization
Maximum number of stops and max ground transfer distance can be configured in application.yaml 

# Implementation details
Shortest path is found using dynamic programming approach with termination when we run out of stops.
Ground transfers between airports are calculated using bounded rectangles search against R-tree airports data structure

# Further improvements
- Controller integration test
- Performance tuning