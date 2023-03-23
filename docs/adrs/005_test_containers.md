# ADR-005 : Test Containers for E2E Testing
Created : 22-Mar-2023
## Status
ACCEPTED
## Context
Currently, we have Unit and Integration Tests in place using frameworks like Mockito.
These tests are excellent for setting up flows and integration between services but mock the actual calls which don't represent an actual use case when a user is interacting with them.
We can use multiple ways to set up E2E integration tests, but we must utilize something that is the most representative of an actual environment even in our local environment.
## Decision
We decided to utilise TestContainers for E2E testing. They offer the ability to spin up local containers with images provided as parameters that last as long as the test context lasts.
The images can then be provided as exact replicas of what will be utilised in PROD.
## Consequences
1. Representative E2E testing, allowing to catch issues faster.
2. Setup and Configuration can be tedious.
3. Docker Pull Limit may cause issues when automating runs via pipelines.