# AWS Starter Infrastructure

## Initial Setup
1. Created a new infrastructure directory and `cd infrastructure` into it, get administrator access to your AWS account.
2. Use command `aws configure` to set up the access key for this administrator account via AWS profile. You can use `aws configure list` to check these.
3. Use command `export AWS_PROFILE=profile_name` to export the profile just created into your environment.
4. Install/Upgrade AWS CDK with a pre-requisite that npm must be installed using the command `npm install -g aws-cdk@latest`
5. Run the command `cdk init app --language=typescript` to create your blank project with directory structure inside the folder.
6. Run the command `cdk synth` to see the initial output of what the blank project does. It bootstraps the CDK and creates cdk.out directory.
7. Run the command `cdk bootstrap` to begin successfully using AWS CDK in your project linked to a specific account.

The *cdk.json* file tells the CDK Toolkit how to execute your app.

## Step By Step Infrastructure
### ECR Repository
1. ECR Repository holds Docker Images
2. Part of common infrastructure as it is passed down into other infrastructure stacks.
3. Destroyed if `cdk destroy` is used.

### VPC
1. VPC for each environment you would like to create. Ex : VPC-Stage
2. Part of Backend Infrastructure as it is specific for each environment.
3. 1 Region -> 2 AZ -> 6 Subnets (3 in each AZ).
    1. 2 Private subnets for the DB.
    2. 2 Private subnets for application with NAT gateway (Security Best Practice)
    3. 2 Public subnets for Nat Gateway (Redundant for High Availability)
4. Destroyed if `cdk destroy` is used.

### RDS
1. Decided to use RDS Aurora Serverless MySQL ([ADR-006](../docs/adrs/006_rds_aurora_mysql_serverless.md)).
2. A Secret is held within secrets manager to hold the DB password.
3. Only the versions supported by this command can be utilised : `aws rds describe-db-engine-versions --engine aurora-mysql --filters Name=engine-mode,Values=serverless`
4. Scaling policy is necessary to set to avoid extra costs when scaling up.
5. Usage of Data API and AWS Query Editor to connect with the DB instead of Bastion Host.
6. Destroyed if `cdk destroy` is used.

## Useful commands
* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk destroy`     DESTROYS the infrastructure created 