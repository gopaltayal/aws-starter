# ADR-006 : RDS Aurora MySQL for actual cloud environment DB
Created : 23-Mar-2023
## Status
ACCEPTED
## Context
MySQL is utilised as the DB engine but as we move the cloud we have the choice between RDS MySQL and RDS Aurora MySQL.
Which Engine makes more sense?
## Decision
We decided to use Aurora MySQL as our underlying engine in AWS Cloud due to the following reasons:
1. **Higher Performance**: Aurora MySQL is designed to offer higher performance than RDS MySQL. It is designed to scale out to support high traffic workloads, and it uses a distributed storage system that can provide higher I/O performance.
2. **Availability**: Aurora MySQL is designed for high availability. It uses a distributed architecture that can survive the failure of multiple nodes without impacting the availability of the database. It also provides automatic failover and can quickly recover from node failures.
3. **Scalability**: Aurora MySQL is designed to scale out to support high traffic workloads. It can add new nodes to the database cluster to handle increased traffic and can automatically adjust to changing workload demands.
4. **Cost-effective**: Aurora MySQL can be more cost-effective than RDS MySQL, especially for workloads that require high availability and scalability. Aurora MySQL uses a distributed storage system that can reduce the amount of storage needed, and it can also scale out to handle more traffic without the need for expensive hardware upgrades.
## Consequences
1. MySQL RDS may be a better fit for smaller workloads or budgets. 
2. Aurora has some advanced features not available in MySQL RDS, such as automatic database failover, faster database cloning, and global database replication. 
3. Aurora is based on MySQL but has some differences in syntax and features and for a lift and shift RDS MySQL is the better choice.