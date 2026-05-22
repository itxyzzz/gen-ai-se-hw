# How To Run

## Prerequisites

- Java 17
- Maven 3.9+
- PowerShell 7 or Windows PowerShell for managed demo scripts
- Optional: Postman for the API collection

## Build And Test

```bash
cd homework-2
mvn test jacoco:report
```

Expected result:

- Maven exits with `BUILD SUCCESS`.
- Surefire reports all tests passing.
- JaCoCo report is generated under `target/site/jacoco/index.html`.

## Run The API

```bash
cd homework-2
mvn spring-boot:run
```

The API listens on:

`http://localhost:8080`

## Managed Local Server

Start:

```powershell
cd homework-2
./demo/start.ps1
```

Stop:

```powershell
./demo/stop.ps1
```

Restart:

```powershell
./demo/restart.ps1
```

Logs are written to:

- `target/app.out.log`
- `target/app.err.log`

## Smoke Checks

Create a ticket with manual category and priority:

```bash
curl -X POST http://localhost:8080/tickets ^
  -H "Content-Type: application/json" ^
  -d "{\"customer_id\":\"CUST-SMOKE-1\",\"customer_email\":\"smoke@example.com\",\"customer_name\":\"Smoke Tester\",\"subject\":\"Cannot access account\",\"description\":\"I cannot access my account after resetting the password.\",\"category\":\"account_access\",\"priority\":\"high\",\"status\":\"new\",\"metadata\":{\"source\":\"api\",\"device_type\":\"desktop\"}}"
```

Create a ticket with default auto-classification:

```bash
curl -X POST http://localhost:8080/tickets ^
  -H "Content-Type: application/json" ^
  -d "{\"customer_id\":\"CUST-AUTO-SMOKE-1\",\"customer_email\":\"auto-smoke@example.com\",\"customer_name\":\"Auto Smoke Tester\",\"subject\":\"Production down security issue\",\"description\":\"Production down security incident is critical and blocking checkout.\",\"status\":\"new\",\"metadata\":{\"source\":\"api\",\"device_type\":\"desktop\"}}"
```

Explicitly auto-classify an existing ticket:

```bash
curl -X POST http://localhost:8080/tickets/{id}/auto-classify
```

List tickets:

```bash
curl http://localhost:8080/tickets
```

OpenAPI documentation:

```bash
curl -I http://localhost:8080/api-docs
curl http://localhost:8080/v3/api-docs
```

Import CSV:

```bash
curl -X POST http://localhost:8080/tickets/import -F "file=@demo/sample_tickets.csv"
curl -X POST http://localhost:8080/tickets/import -F "file=@demo/classification_tickets.csv"
```

## Classification Flags

The default local configuration enables auto-classification and manual override support for create and import flows:

```properties
tickets.classification.auto-classify-on-create=true
tickets.classification.auto-classify-on-import=true
tickets.classification.allow-create-manual-override=true
tickets.classification.allow-import-manual-override=true
```

## Troubleshooting

- If port `8080` is busy, stop the existing process or run Maven with `-Dspring-boot.run.arguments=--server.port=8081`.
- If Maven cannot resolve dependencies, confirm network or local repository access.
- If Postman file upload paths fail, reselect files from the local `demo/` directory.
