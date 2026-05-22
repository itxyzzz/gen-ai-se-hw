# OpenAPI Swagger UI Design

Date: 2026-05-02

## Goal

Add reviewer-facing OpenAPI documentation to the Homework 2 Spring Boot API with one clear browser entrypoint: `/api-docs`.

## Approach

Use Springdoc's Spring Boot 3 WebMVC starter because it auto-generates an OpenAPI 3 document from the existing Spring MVC controllers and serves Swagger UI without custom static assets. Configure Swagger UI to use `/api-docs` as the human-facing entrypoint; Springdoc redirects that route to its bundled Swagger UI asset. Leave Springdoc's generated JSON endpoint available at `/v3/api-docs` because Swagger UI depends on it internally and automated tests can verify the contract there.

## Scope

In scope:

- Add `springdoc-openapi-starter-webmvc-ui` to `homework-2/pom.xml`.
- Configure `springdoc.swagger-ui.path=/api-docs`.
- Add basic OpenAPI metadata for the Homework 2 API title, version, and description.
- Add MockMvc coverage proving `/api-docs` redirects or resolves successfully and `/v3/api-docs` exposes ticket endpoints.
- Update reviewer docs and the Homework 2 changelog.

Out of scope:

- Adding detailed `@Operation`, response, and schema annotations to every endpoint.
- Changing existing ticket API behavior or response bodies.
- Moving existing endpoints under a new base path.

## Testing

Add a focused integration test class for API documentation. The tests should fail before the Springdoc dependency/configuration exists, then pass once the integration is added. Run the new test first, then the full Maven verification gate.

## Documentation Impact

Update `README.md`, `HOWTORUN.md`, and `API_REFERENCE.md` so reviewers know that Swagger UI is available at `http://localhost:8080/api-docs`.
