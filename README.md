# Documentation for AWS Starter Project

## Introduction
The project is a culmination of various development related practices and terminology.
It acts as a reference point for certain pieces of code that you would require in your day-to-day development activities like:
1. How to practice Hexagonal Architecture?
2. How to write unit tests, integration tests and end-to-end tests?
3. How to set up a docker-compose file for dependencies like the database?
4. How to set up a docker file for deployment of the application?
5. How to write GIT commits?

Besides this, the main aim of the project is to introduce best practices for dealing with the most common AWS resources that you would interact with.
For example :
1. How to set up a code pipeline?
2. How to maintain the infrastructure via CDK?
3. How to use local stack for testing?
4. How to connect to various environments like staging and prod?
5. How to utilise common AWS services like VPC, RDS, S3?
6. How to deploy to ECS?

In parallel, the project will also be implemented in a Serverless re-design. This will be done so that both the server-dependent version and the serverless version lie in the same code base, in order to compare and highlight the differences between both.
These architectures will further be documented using best practices for documentation like ADRs and C4 model.

My suggestion would be to follow the project commit after commit to understand what was added at what step and complement it with the corresponding documentation.
Please feel free to add comments on which aspect of it is unclear or what could be done to improve!

## ADRs
- [ADR-001](/docs/adrs/001_kotlin_vs_java.md)
- [ADR-002](/docs/adrs/002_hexagonal_architecture.md)
- [ADR-003](/docs/adrs/003_why_mysql.md)
- [ADR-004](/docs/adrs/004_liquibase_for_schema_management.md)
- [ADR-005](/docs/adrs/005_test_containers.md)
- [ADR-006](/docs/adrs/006_rds_aurora_mysql_serverless.md)

## C4 Model

## Other Documents
- [Infrastructure Documentation](/infrastructure/README.md)
- [How to run the application locally?](/docs/local_setup.md)