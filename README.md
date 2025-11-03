# Transakt Banking

This is a banking system application I am starting to develop to learn microservice architecture in Java with Spring Boot and the Spring Cloud projects.

It will be a simple banking style system, using event-driven architecture and loose coupling where possible, and will have auth handled using OAuth2 and OIDC with Keycloak.

# Main features

- Java 21 and Spring Boot 3.5 for the microservices, following good practices and SOLID.
- Event-driven architecture using Kafka and Spring cloud stream for async communication between services. 
- ApplicationEventPublisher for sending local events for async processing.
- Keycloak for authentication using OAuth2.0 and OIDC, with a PKCE client for secure end user authentication and authorization.
- Use of the java Keycloak admin client library for secure access to manage users via a service account client using client-credentials grant type flow. 
- Spring cloud gateway server for secure access to each microservice through one entrypoint, which will handle authentication checking, passing current auth information downstream via headers.
- Spring cloud config server for centralised microservice configuration.
- Email sending using the Jakarta mail API. 

# Microservices

- **customers** --> Will manage bank customer information, on customer sign-up it will register customers in Keycloak as a user to authenticate.
- **accounts** --> Will handle bank accounts, balances, etc.
- **transactions** --> Will record and manage transactions and communicate with accounts-service via events passed through Kafka.
- **config-server** --> Will externalise the configuration of the microservices to one place.
- **gateway-server** --> Will handle securely providing access to inner microservices to the end users.