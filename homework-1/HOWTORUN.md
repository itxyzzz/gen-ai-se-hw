# How to Run the Application

## Prerequisites

- Java 17 or newer
- Maven 3.9 or newer

## Run Tests

```powershell
mvn test
```

## Start the API

Preferred Windows workflow:

```powershell
.\demo\start.ps1
```

Stop the managed app:

```powershell
.\demo\stop.ps1
```

Restart with a fresh build:

```powershell
.\demo\restart.ps1
```

The scripts build the latest compiled classes and runtime dependencies, then launch the API without relying on the Spring Boot fat-JAR packaging step.

Manual fallback:

```powershell
mvn spring-boot:run
```

The API runs at `http://localhost:8080`.

## Try a Request

```powershell
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d "{\"fromAccount\":\"ACC-12345\",\"toAccount\":\"ACC-67890\",\"amount\":100.50,\"currency\":\"USD\",\"type\":\"transfer\"}"
```

```powershell
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d "{\"toAccount\":\"ACC-22222\",\"amount\":250.00,\"currency\":\"USD\",\"type\":\"deposit\"}"
```

```powershell
curl -X POST http://localhost:8080/transactions -H "Content-Type: application/json" -d "{\"fromAccount\":\"ACC-22222\",\"amount\":40.25,\"currency\":\"USD\",\"type\":\"withdrawal\"}"
```

```powershell
curl http://localhost:8080/transactions
```

```powershell
curl http://localhost:8080/accounts/ACC-12345/balance
```

```powershell
curl http://localhost:8080/accounts/ACC-12345/summary
```

## Transaction Type Rules

- `transfer` requires `fromAccount` and `toAccount`
- `deposit` requires only `toAccount`
- `withdrawal` requires only `fromAccount`
- Supplying the forbidden account field returns `400 Bad Request` with an explicit validation message
