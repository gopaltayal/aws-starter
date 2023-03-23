# ADR-002 : Usage of Hexagonal Architecture
Created : 22-Mar-2023
## Status
ACCEPTED
## Context
How to structure the project to make it more maintainable and easier to on-board new development with clear separation of concerns.
## Decision
To support new methodologies, we decided to implement and structure the code base using hexagonal architecture practices.
A source of resources can be found for the same:

1. https://blog.allegro.tech/2020/05/hexagonal-architecture-by-example.html
2. https://reflectoring.io/spring-hexagonal/
## Consequences
1. The code structure may seem like a lot of effort in the beginning but helps later in the long run.
2. Clear separation of concerns among ports and adapters.
3. Replace-ability of adapters from one framework/language to another.
4. Added management of models between layers to enforce clear separation of concerns.
5. Steep Learning Curve.