import {aws_ecr as ecr, RemovalPolicy, Stack, StackProps} from 'aws-cdk-lib';
import {Construct} from 'constructs';

export class CommonInfrastructureStack extends Stack {
    constructor(scope: Construct, id: string, props?: StackProps) {
        super(scope, id, props);

        // ECR Repository to hold the Docker Images
        this.createEcrRepo()
    }

    private createEcrRepo() {
        new ecr.Repository(this, 'aws-starter-repo', {
            repositoryName: "aws-starter-repo",
            removalPolicy: RemovalPolicy.DESTROY
        });
    }
}