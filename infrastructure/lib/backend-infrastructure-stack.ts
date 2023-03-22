import {aws_ec2 as ec2, Stack, StackProps} from 'aws-cdk-lib';
import {Construct} from "constructs";
import {EnvOptions} from "../bin/infrastructure";
import {SubnetType, VpcProps} from "aws-cdk-lib/aws-ec2";
import {CommonInfrastructureStack} from "./common-infrastructure-stack";

const nameIdPrefix = "aws-starter"

export class BackendInfrastructureStack extends Stack {
    // Public as this VPC will be used further by elements across the stack
    // In other scopes it could also be private or passed along as method return type and method argument to which ever component might need it for creation.
    public vpc: ec2.Vpc;

    constructor(scope: Construct, id: string, envOptions: EnvOptions, commonStack: CommonInfrastructureStack, props?: StackProps) {
        super(scope, id, props);

        // Create a VPC with private and public subnets to hold the database, application and the NAT Gateway.
        this.createVPC(envOptions)
    }

    // Creates a VPC which spans 2 availability zones.
    private createVPC(envOptions: EnvOptions) {
        let vpcProps: VpcProps = {
            cidr: envOptions.vpcCidr,
            maxAzs: 2,
            // This is per AZ
            subnetConfiguration: [
                {
                    // Security Best Practice to communicate only via the NAT
                    subnetType: SubnetType.PRIVATE_WITH_NAT,
                    name: "private-application"
                },
                {
                    subnetType: SubnetType.PRIVATE_ISOLATED,
                    name: "private-database"
                },
                {
                    subnetType: SubnetType.PUBLIC,
                    name: "public-nat"
                }
            ],
            // 1 Per AZ for high availability
            natGateways: 2
        }
        this.vpc = new ec2.Vpc(this, `${nameIdPrefix}-vpc-${envOptions.environmentName}`, vpcProps)
    }
}