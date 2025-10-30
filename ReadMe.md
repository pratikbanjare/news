THe project is to use the endpoint provided by news api and create personalized new feed out of it.

https://newapi.org

Setup

set these 3 on application.properties file

api.key=API_KEY

url.headline=https://newsapi.org/v2/top-headlines

url.everything=https://newsapi.org/v2/everything

## Swagger UI

This project now includes OpenAPI/Swagger UI using springdoc-openapi. After you start the application (for example with `mvnw.cmd spring-boot:run`), the API docs and UI are available at:

- OpenAPI JSON: http://localhost:8080/v3/api-docs
- Swagger UI (interactive): http://localhost:8080/swagger-ui/index.html

If you changed the server port in `application.properties`, replace `8080` with that port.

Notes:
- I updated the Maven `pom.xml` to add `org.springdoc:springdoc-openapi-starter-webmvc-ui` and added a minimal `OpenApiConfig` class under `com.project.news.config` to provide API metadata.
- I also set the project's `java.version` property to `17` to allow local compilation where Java 21 is not available; change it back to `21` if your environment supports it.
