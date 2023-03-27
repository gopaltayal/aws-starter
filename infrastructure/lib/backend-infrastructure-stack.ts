import {
    aws_ec2 as ec2,
    aws_rds as rds,
    aws_secretsmanager as secretsManager,
    RemovalPolicy,
    Stack,
    StackProps
} from 'aws-cdk-lib';
import {Construct} from "constructs";
import {EnvOptions} from "../bin/infrastructure";
import {SubnetType, VpcProps} from "aws-cdk-lib/aws-ec2";
import {CommonInfrastructureStack} from "./common-infrastructure-stack";
import {SecretProps} from "aws-cdk-lib/aws-secretsmanager";
import {
    AuroraCapacityUnit,
    AuroraMysqlEngineVersion,
    Credentials,
    DatabaseClusterEngine,
    ServerlessCluster,
    ServerlessClusterProps
} from "aws-cdk-lib/aws-rds";

const nameIdPrefix = "aws-starter"

export class BackendInfrastructureStack extends Stack {
    // Public as this VPC will be used further by elements across the stack
    // In other scopes it could also be private or passed along as method return type and method argument to which ever component might need it for creation.
    public vpc: ec2.Vpc;
    private rds: rds.ServerlessCluster;
    private databaseSecret: secretsManager.Secret;

    constructor(scope: Construct, id: string, envOptions: EnvOptions, commonStack: CommonInfrastructureStack, props?: StackProps) {
        super(scope, id, props);

        // Create a VPC with private and public subnets to hold the database, application and the NAT Gateway.
        this.createVPC(envOptions)

        // Create a database with RDS with MySQL on Aurora
        this.createDatabase(envOptions)
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

    private createDatabase(envOptions: EnvOptions) {
        // Secrets Manager will manage the DB secret, and the applications will gain access to it via this secret
        let dbSecretProps: SecretProps = {
            generateSecretString: {
                // This name can't contain '-' hence had to be named this way
                secretStringTemplate: JSON.stringify({username: `awsStarterAdmin`}),
                generateStringKey: 'password',
                passwordLength: 32,
                excludeCharacters: '/@"\\'
            }
        }
        this.databaseSecret = new secretsManager.Secret(this, `${nameIdPrefix}-db-secret-${envOptions.environmentName}`, dbSecretProps);

        //  Actual DB cluster that will have eventual consistency
        let serverlessClusterProps: ServerlessClusterProps = {
            // This was the engine version that was available in serverless mode when checked with the command:
            // 'aws rds describe-db-engine-versions --engine aurora-mysql --filters Name=engine-mode,Values=serverless'
            engine: DatabaseClusterEngine.auroraMysql({version: AuroraMysqlEngineVersion.of("5.7.mysql_aurora.2.08.2")}),
            // This name can't contain '-' hence had to be named this way
            defaultDatabaseName: `awsStarterDb${envOptions.environmentName}`,
            removalPolicy: RemovalPolicy.DESTROY,
            vpc: this.vpc,
            vpcSubnets: {subnetType: SubnetType.PRIVATE_ISOLATED, onePerAz: true},
            credentials: Credentials.fromSecret(this.databaseSecret),
            // Otherwise the minimum is 2 and maximum is 16 starts with 8 incurring costs
            scaling: {minCapacity: AuroraCapacityUnit.ACU_2, maxCapacity: AuroraCapacityUnit.ACU_2},
            // Allows you to connect via Query Editor and Data API, without bastion host
            enableDataApi: true
        }
        this.rds = new ServerlessCluster(this, `${nameIdPrefix}-db-${envOptions.environmentName}`, serverlessClusterProps)
    }
}