# Transakt Banking

This is a banking system application I am starting to develop to learn microservice architecture in Java with Spring Boot and the Spring Cloud projects.

It will be a simple banking style system, using event-driven architecture and loose coupling where possible, and will have auth handled using OAuth2 and OIDC with Keycloak.

# Microservices

- **customers** --> Will manage bank customer information, on sign up it will register customers in Keycloak as a user to authenticate.
- **accounts** --> Will handle bank accounts, balances, etc.
- **transactions** --> Will record and manage transactions and communicate with accounts-service via events passed through Kafka.
- **config-server** --> Will externalise the configuration of the microservices to one place.
- **gateway-server** --> (NOT STARTED) Will handle securely providing access to inner microservices to the end users.

