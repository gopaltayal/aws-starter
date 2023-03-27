import {
    aws_rds as rds,
    aws_secretsmanager as secretsManager,
    RemovalPolicy,
    Stack,
    StackProps
} from 'aws-cdk-lib';
import {Construct} from "constructs";
import {EnvOptions} from "../bin/infrastructure";
import {SubnetType} from "aws-cdk-lib/aws-ec2";
import {SecretProps} from "aws-cdk-lib/aws-secretsmanager";
import {
    AuroraCapacityUnit,
    AuroraMysqlEngineVersion,
    Credentials,
    DatabaseClusterEngine,
    ServerlessCluster,
    ServerlessClusterProps
} from "aws-cdk-lib/aws-rds";
import {NetworkStack} from "./network-stack";
import {NAME_ID_PREFIX} from "./common-stack";

export class DatabaseStack extends Stack {
    private rds: rds.ServerlessCluster;
    private databaseSecret: secretsManager.Secret;

    constructor(scope: Construct, id: string, envOptions: EnvOptions, networkStack: NetworkStack, props?: StackProps) {
        super(scope, id, props);

        // Create a database with RDS with MySQL on Aurora
        this.createDatabase(envOptions, networkStack)
    }

    private createDatabase(envOptions: EnvOptions, networkStack: NetworkStack) {
        // Secrets Manager will manage the DB secret, and the applications will gain access to it via this secret
        let dbSecretProps: SecretProps = {
            generateSecretString: {
                // This name can't contain '-' hence had to be named this way
                secretStringTemplate: JSON.stringify({username: `awsStarterAdmin`}),
                generateStringKey: 'password',
                passwordLength: 32,
                // Only printable ASCII characters besides '/', '@', '"', ' ' may be used
                excludeCharacters: '/@"\\'
            }
        }
        this.databaseSecret = new secretsManager.Secret(this, `${NAME_ID_PREFIX}-db-secret-${envOptions.environmentName}`, dbSecretProps);

        //  Actual DB cluster that will have eventual consistency
        let serverlessClusterProps: ServerlessClusterProps = {
            // This was the engine version that was available in serverless mode when checked with the command:
            // 'aws rds describe-db-engine-versions --engine aurora-mysql --filters Name=engine-mode,Values=serverless'
            engine: DatabaseClusterEngine.auroraMysql({version: AuroraMysqlEngineVersion.of("5.7.mysql_aurora.2.08.2")}),
            // This name can't contain '-' hence had to be named this way
            defaultDatabaseName: `awsStarterDb${envOptions.environmentName}`,
            removalPolicy: RemovalPolicy.DESTROY,
            vpc: networkStack.getVpc(),
            vpcSubnets: {subnetType: SubnetType.PRIVATE_ISOLATED, onePerAz: true},
            credentials: Credentials.fromSecret(this.databaseSecret),
            // Otherwise the minimum is 2 and maximum is 16 starts with 8 incurring costs
            scaling: {minCapacity: AuroraCapacityUnit.ACU_2, maxCapacity: AuroraCapacityUnit.ACU_2},
            // Allows you to connect via Query Editor and Data API, without bastion host
            enableDataApi: true
        }
        this.rds = new ServerlessCluster(this, `${NAME_ID_PREFIX}-db-${envOptions.environmentName}`, serverlessClusterProps)
    }
}