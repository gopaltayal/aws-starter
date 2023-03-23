# ADR-003 : Usage of MySQL as the DB of Choice for Server based applications
Created : 22-Mar-2023
## Status
ACCEPTED
## Context
Which Database should be used to store and query data related to the application with choices between MySQL, PostgreSQL, MongoDB etc.
## Decision
We decided to use MySQL due to the transactional nature of data. A relational DB made more sense at the time of starting the project.
It is also based on the experience that one has with it and ease of understanding standard SQL queries.
## Consequences
1. Data will be stored in tables instead of fancy buzzwords which solves the business case.
2. Maintainability will be impacted as this needs maintenance and patching and monitoring.
3. An eventual Serverless design will need changes but [ADR-002](002_hexagonal_architecture.md) should help with that aspect.