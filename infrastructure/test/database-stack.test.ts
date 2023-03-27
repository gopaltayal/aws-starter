import * as cdk from 'aws-cdk-lib';
import * as NetworkInfrastructure from '../lib/network-stack';
import * as DatabaseInfrastructure from '../lib/database-stack';
import {Template} from "aws-cdk-lib/assertions";
import {EnvOptions} from "../bin/infrastructure";

test('setup rds mysql aurora serverless', () => {
    const app = new cdk.App();
    const envOptions: EnvOptions = {
        environmentName: "test",
        vpcCidr: "10.1.0.0/16"
    }
    const networkStack = new NetworkInfrastructure.NetworkStack(app, 'NetworkStack', envOptions);
    // WHEN
    const stack = new DatabaseInfrastructure.DatabaseStack(app, 'MyTestStack', envOptions, networkStack);
    // THEN
    const template = Template.fromStack(stack);
    template.resourceCountIs("AWS::SecretsManager::Secret", 1)
    template.hasResourceProperties('AWS::SecretsManager::SecretTargetAttachment', {
        TargetType: "AWS::RDS::DBCluster"});
    template.resourceCountIs("AWS::RDS::DBSubnetGroup", 1)
    template.hasResourceProperties('AWS::EC2::SecurityGroup', {
        GroupDescription: "RDS security group"});
    template.hasResourceProperties('AWS::RDS::DBCluster', {
        Engine: "aurora-mysql",
        DBClusterParameterGroupName: "default.aurora-mysql5.7",
        EnableHttpEndpoint: true,
        EngineMode: "serverless",
        EngineVersion: "5.7.mysql_aurora.2.08.2",
        ScalingConfiguration: {
            AutoPause: true,
            MaxCapacity: 2,
            MinCapacity: 2
        }});
});
