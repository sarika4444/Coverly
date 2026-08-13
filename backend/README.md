# Coverly Backend

Coverly is an insurance lifecycle prototype combining Policy, Claims, Billing and the Sentinel risk/fraud decision engine.

## Stack

- Java 21
- Spring Boot 4.1.0
- Maven
- Spring WebMVC
- Bean Validation
- In-memory prototype storage
- Gosu rule prototypes

## Important

This version intentionally excludes PostgreSQL/JPA so the complete business/API layer can be demonstrated quickly. Database persistence can be added later without changing the frontend API contract.

## Run

Windows:
`mvnw.cmd spring-boot:run`

Mac/Linux:
`./mvnw spring-boot:run`

## Main APIs

- GET /api/dashboard/summary
- POST /api/customers
- GET /api/customers
- GET /api/customers/{id}/360
- POST /api/policies
- GET /api/policies
- PATCH /api/policies/{id}/status?status=ACTIVE
- POST /api/claims
- GET /api/claims
- PATCH /api/claims/{id}/status?status=SETTLED
- POST /api/billing
- GET /api/billing
- POST /api/billing/{id}/payment?amount=10000
- GET /api/sentinel/customer/{customerNumber}
- GET /api/sentinel/claim?customerNumber=CUS-00001&policyNumber=POL-00001&claimAmount=800000
- GET /api/audit
- GET /api/audit/{referenceId}

## Insurance lifecycle

Policy:
QUOTED -> ISSUED -> ACTIVE -> SUSPENDED/EXPIRED/CANCELLED

Claim:
SUBMITTED -> APPROVED or UNDER_REVIEW -> SETTLED/REJECTED

Billing:
OVERDUE/PARTIALLY_PAID -> PAID

## Sentinel

Sentinel combines:
- policy state
- claim-to-insured-value ratio
- claim amount
- previous claims
- overdue billing
- previous high-value claims

It returns:
- risk score
- fraud score
- risk level
- decision
- flags
- explanations

## Gosu

`gosu-rules/` contains insurance-rule prototypes intended to mirror Guidewire/Gosu rule concepts. The current Spring prototype does not execute Gosu directly.
