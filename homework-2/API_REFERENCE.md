# API Reference

Base URL: `http://localhost:8080`

All JSON fields use `snake_case`.

## Ticket Schema

```json
{
  "id": "UUID",
  "customer_id": "string",
  "customer_email": "email",
  "customer_name": "string",
  "subject": "string, 1-200 chars",
  "description": "string, 10-2000 chars",
  "category": "account_access | technical_issue | billing_question | feature_request | bug_report | other",
  "priority": "urgent | high | medium | low",
  "status": "new | in_progress | waiting_customer | resolved | closed",
  "created_at": "datetime",
  "updated_at": "datetime",
  "resolved_at": "datetime or null",
  "assigned_to": "string or null",
  "tags": ["string"],
  "metadata": {
    "source": "web_form | email | api | chat | phone",
    "browser": "string or null",
    "device_type": "desktop | mobile | tablet or null"
  }
}
```

## Error Shapes

Validation:

```json
{
  "error": "Validation failed",
  "details": [
    {"field": "customer_email", "message": "Email must be valid"}
  ]
}
```

General:

```json
{
  "error": "Not found",
  "message": "Ticket not found"
}
```

## POST /tickets

Creates a ticket. Returns `201`.

```bash
curl -X POST http://localhost:8080/tickets \
  -H "Content-Type: application/json" \
  -d '{"customer_id":"CUST-1","customer_email":"ada@example.com","customer_name":"Ada Lovelace","subject":"Cannot access account","description":"I cannot access my account after resetting my password.","category":"account_access","priority":"high","status":"new","assigned_to":"agent-1","tags":["login"],"metadata":{"source":"web_form","browser":"Firefox","device_type":"desktop"}}'
```

## GET /tickets

Lists tickets. Optional filters:

`category`, `priority`, `status`, `customer_id`, `customer_email`, `assigned_to`, `source`, `tag`

```bash
curl "http://localhost:8080/tickets?category=account_access&priority=high&source=web_form"
```

## GET /tickets/{id}

Returns one ticket or `404`.

```bash
curl http://localhost:8080/tickets/11111111-1111-1111-1111-111111111111
```

## PUT /tickets/{id}

Replaces editable ticket fields. Preserves `id` and `created_at`; updates `updated_at`.

```bash
curl -X PUT http://localhost:8080/tickets/{id} \
  -H "Content-Type: application/json" \
  -d '{"customer_id":"CUST-1","customer_email":"ada@example.com","customer_name":"Ada Lovelace","subject":"Cannot access account","description":"The access issue has been resolved by support.","category":"account_access","priority":"high","status":"resolved","assigned_to":"agent-2","tags":["login"],"metadata":{"source":"web_form","browser":"Firefox","device_type":"desktop"}}'
```

## DELETE /tickets/{id}

Deletes a ticket. Returns `204` or `404`.

```bash
curl -X DELETE http://localhost:8080/tickets/{id}
```

## POST /tickets/import

Imports CSV, JSON, or XML as multipart form data. Field `file` is required. Optional field `format` can be `csv`, `json`, or `xml`.

```bash
curl -X POST http://localhost:8080/tickets/import -F "file=@demo/sample_tickets.csv"
curl -X POST http://localhost:8080/tickets/import -F "file=@demo/sample_tickets.json"
curl -X POST http://localhost:8080/tickets/import -F "file=@demo/sample_tickets.xml"
```

Import summary:

```json
{
  "total_records": 3,
  "successful": 2,
  "failed": 1,
  "created_ticket_ids": ["uuid"],
  "errors": [
    {"record": 2, "field": "customer_email", "message": "Email must be valid"}
  ]
}
```

CSV headers:

`customer_id,customer_email,customer_name,subject,description,category,priority,status,resolved_at,assigned_to,tags,metadata_source,metadata_browser,metadata_device_type`

For CSV `tags`, separate multiple tags with semicolons.
