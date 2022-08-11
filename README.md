# Getting Started

REST API that takes as input two IATA airport codes and provides as output a route between these two airports

# Running
- Make sure docker desktop is installed & running
- Run `docker-compose up`
- Navigate to [Open API documentation](http://localhost:8080/swagger-ui/#/) and play with an API

# API
GET http://localhost:8080/?airport1=TLL&airport2=TAY
Response: TLL=>HEL->TAY (344.77 km)

# Implementation details

# Further improvements
- Controller integration test
- Performance tuning