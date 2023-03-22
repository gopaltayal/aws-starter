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

## Useful commands
* `npm run build`   compile typescript to js
* `npm run watch`   watch for changes and compile
* `npm run test`    perform the jest unit tests
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk destroy`     DESTROYS the infrastructure created 