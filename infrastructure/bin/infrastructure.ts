#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import {CommonInfrastructureStack} from '../lib/common-infrastructure-stack';
import {BackendInfrastructureStack} from "../lib/backend-infrastructure-stack";

export interface EnvOptions {
    // Name of the environment like stage/prod
    readonly environmentName: string;
    // CIDR block for this VPC
    readonly vpcCidr: string;
}

// External Parameters that will be needed by the infrastructure
const stageEnv : EnvOptions = {
    environmentName: "stage",
    vpcCidr: "10.1.0.0/16"
}

// Main App that binds all your stacks together
// An App is a container for one or more stacks: it serves as each stack's scope.
// Stacks within a single App can easily refer to each other's resources.
const app = new cdk.App();

// Options to provide the account and region where the deployment will take place
// Utilises the CLI profile to find it instead of hard-coding this aspect here.
const cdkOptions: cdk.StackProps = {
    env: {
        account: process.env.CDK_DEFAULT_ACCOUNT,
        region: process.env.CDK_DEFAULT_REGION
    }
};

// The stacks that this application will stitch together
const commonStack = new CommonInfrastructureStack(app, 'aws-starter-common-infra', cdkOptions);
new BackendInfrastructureStack(app, 'aws-starter-backend-stage', stageEnv, commonStack, cdkOptions)