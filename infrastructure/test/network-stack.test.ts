import * as cdk from 'aws-cdk-lib';
import * as BackendInfrastructure from '../lib/network-stack';
import {Template} from "aws-cdk-lib/assertions";
import {EnvOptions} from "../bin/infrastructure";

test('setup vpc infrastructure', () => {
    const app = new cdk.App();
    const envOptions: EnvOptions = {
        environmentName: "test",
        vpcCidr: "10.1.0.0/16"
    }
    // WHEN
    const stack = new BackendInfrastructure.NetworkStack(app, 'MyTestStack', envOptions);
    // THEN
    const template = Template.fromStack(stack);
    template.hasResourceProperties('AWS::EC2::VPC', {
        CidrBlock: envOptions.vpcCidr,
        Tags: [{
            Key: "Name",
            Value: "MyTestStack/aws-starter-vpc-test"
        }]
    });
    template.hasResourceProperties('AWS::EC2::Subnet', {
        Tags: [{
            Key: "aws-cdk:subnet-name",
            Value: "private-application"
        },
            {
                Key: "aws-cdk:subnet-type",
                Value: "Private"
            },
            {
                Key: "Name",
                Value: "MyTestStack/aws-starter-vpc-test/private-applicationSubnet1"
            }]
    });
    template.hasResourceProperties('AWS::EC2::Subnet', {
        Tags: [{
            Key: "aws-cdk:subnet-name",
            Value: "private-database"
        },
            {
                Key: "aws-cdk:subnet-type",
                Value: "Isolated"
            },
            {
                Key: "Name",
                Value: "MyTestStack/aws-starter-vpc-test/private-databaseSubnet1"
            }]
    });
    template.hasResourceProperties('AWS::EC2::Subnet', {
        Tags: [{
            Key: "aws-cdk:subnet-name",
            Value: "public-nat"
        },
            {
                Key: "aws-cdk:subnet-type",
                Value: "Public"
            },
            {
                Key: "Name",
                Value: "MyTestStack/aws-starter-vpc-test/public-natSubnet1"
            }]
    });
    template.resourceCountIs("AWS::EC2::Subnet", 6)
    template.resourceCountIs("AWS::EC2::RouteTable", 6)
    template.resourceCountIs("AWS::EC2::SubnetRouteTableAssociation", 6)
    template.resourceCountIs("AWS::EC2::Route", 4)
    template.resourceCountIs("AWS::EC2::EIP", 2)
    template.resourceCountIs("AWS::EC2::NatGateway", 2)
    template.resourceCountIs("AWS::EC2::InternetGateway", 1)
    template.resourceCountIs("AWS::EC2::VPCGatewayAttachment", 1)
});