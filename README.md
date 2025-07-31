# Payment Batch Reconciliation API

## Overview

Design and implement a REST API using Spring Boot that handles payment batch processing and reconciliation with
invoices. This project tests your ability to model business domains, design RESTful APIs, and implement core business
logic.

## Business Context

Our company processes payments in batches to optimize transaction costs. Each payment batch contains multiple payment
requests that need to be reconciled against existing invoices to ensure accuracy before processing.

## Core Requirements

### Domain Models

You'll need to model these key entities:

#### Invoice

* Unique identifier
* Invoice number
* Amount
* Currency
* Vendor/supplier information
* Due date
* Status (pending, paid, overdue)

#### Payment Batch

* Unique identifier
* Batch reference number
* Creation timestamp
* Total amount
* Status (created, submitted, reconciled, processed, failed)
* List of payment requests

#### Payment Request

* Unique identifier
* Amount
* Currency
* Reference to invoice (invoice ID or invoice number)
* Description/memo

#### Reconciliation Result

* Batch identifier
* Overall reconciliation status
* List of reconciliation items (per payment request)
* Summary of discrepancies

### Required API Endpoints

#### Invoice Management

```
GET /api/invoices
GET /api/invoices/{id}
POST /api/invoices
PUT /api/invoices/{id}
```

#### Payment Batch Operations

```
POST /api/payment-batches
GET /api/payment-batches/{id}
GET /api/payment-batches
PUT /api/payment-batches/{id}/submit
```

#### Reconciliation

```
POST /api/payment-batches/{id}/reconcile
GET /api/payment-batches/{id}/reconciliation-result
```

### Core Business Logic

#### Payment Batch Submission Rules:

* A batch can only be submitted if it's in "created" status
* All payment requests must reference valid invoices
* Batch total must equal sum of individual payment requests

#### Reconciliation Logic:

* Match payment requests to invoices by ID/number
* Validate currency matches
* Check if payment amount matches invoice amount exactly
* Identify discrepancies (overpayment, underpayment, missing invoices)
* Update batch status based on reconciliation results

### Expected Deliverables

* REST API Implementation
* All endpoints working with proper HTTP status codes
* Request/response DTOs
* Basic input validation
* Business LogicPayment batch creation and submission
* Reconciliation engine implementation
* Proper status management
* Data LayerEntity classes with relationships
* Repository interfaces
* Basic data persistence (H2 in-memory is fine)
* Error HandlingAppropriate exception handling
* Meaningful error responses

### Technical Constraints

* Use Spring Boot 2.7+ or 3.x
* Implement using Spring Web, Spring Data JPA
* Include basic validation (Bean Validation)
* Use H2 or any embedded database
* No authentication required (focus on business logic)
* Include basic unit tests for core business logic

### Sample Request/Response Examples

* Create Payment Batch

```json
POST /api/payment-batches
{
  "batchReference": "BATCH-2024-001",
  "paymentRequests": [
    {
      "amount": 1000.00,
      "currency": "USD",
      "invoiceReference": "INV-001",
      "description": "Payment for services rendered"
    },
    {
      "amount": 750.50,
      "currency": "USD",
      "invoiceReference": "INV-002",
      "description": "Equipment purchase"
    }
  ]
}
```

### Reconciliation Result

```json
GET /api/payment-batches/{id}/reconciliation-result
{
  "batchId": "123",
  "overallStatus": "PARTIALLY_RECONCILED",
  "totalRequests": 2,
  "reconciledCount": 1,
  "discrepancyCount": 1,
  "reconciliationItems": [
    {
      "paymentRequestId": "456",
      "invoiceId": "789",
      "status": "MATCHED",
      "paymentAmount": 1000.00,
      "invoiceAmount": 1000.00,
      "discrepancy": null
    },
    {
      "paymentRequestId": "457",
      "invoiceId": "790",
      "status": "AMOUNT_MISMATCH",
      "paymentAmount": 750.50,
      "invoiceAmount": 800.00,
      "discrepancy": "UNDERPAYMENT"
    }
  ]
}
```