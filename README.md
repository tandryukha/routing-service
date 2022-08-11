# Getting Started

REST API that takes as input two IATA airport codes and provides as output a route between these two airports

# Running
- Make sure docker desktop is installed & running
- Run `docker-compose up`
- Test application is healthy by navigating to [URL]( http://localhost:8080/?airport1=TLL&airport2=TAY)
- Navigate to [Open API documentation](http://localhost:8080/swagger-ui/#/) and play with an API


# Running without cached layers (during development)
1. Stop and remove existing containers `docker-compose down --rmi all`
2. Start and infrastructure locally in docker compose `docker-compose up --force-recreate`

# API
GET http://localhost:8080/?airport1=TLL&airport2=TAY
Response: TLL=>HEL->TAY (344.77 km)

# Customization
Maximum number of stops and max ground transfer distance can be configured in application.yaml 
# Implementation details

# Further improvements
- Controller integration test
- Performance tuning