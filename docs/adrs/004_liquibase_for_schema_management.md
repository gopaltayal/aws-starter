# ADR-004 : Liquibase for Schema Management
Created : 22-Mar-2023
## Status
ACCEPTED
## Context
The usage of [Hibernate Auto DDL creation](https://docs.spring.io/spring-boot/docs/1.1.0.M1/reference/html/howto-database-initialization.html) in local environment, for the generation of view models in the MySQL DB is considered to be OK.
But it isn't the best way to auto deploy these changes in production environments.
This is not the best way to maintain DDL, DML and DCL for a database as Hibernate auto generation doesn't support complex relation mapping.
## Decision
We will utilise Liquibase in order to add any **new** tables or support DB changes.The key differentiator Liquibase provides a schema changelog. This is a record of the schema changes over time. It allows the database designer to specify changes in schema & enables programmatic upgrade or downgrade of the schema on demand.
## Consequences
1. We have better relational mapping possible via Liquibase.
2. We will have multiple files for each DB change performed which can be tedious to maintain.
## Further Reading
- http://malaguna.github.io/blog/2015/06/09/liquifying-your-project/md) should help with that aspect.