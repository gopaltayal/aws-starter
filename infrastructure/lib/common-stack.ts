import {aws_ecr as ecr, RemovalPolicy, Stack, StackProps} from 'aws-cdk-lib'
import {Construct} from 'constructs'

export const NAME_ID_PREFIX = "aws-starter"

export class CommonStack extends Stack {
    private repository: ecr.Repository
    constructor(scope: Construct, id: string, props?: StackProps) {
        super(scope, id, props)

        // ECR Repository to hold the Docker Images
        this.createEcrRepo()
    }

    private createEcrRepo() {
        this.repository = new ecr.Repository(this, `${NAME_ID_PREFIX}-repo`, {
            repositoryName: `${NAME_ID_PREFIX}-repo`,
            removalPolicy: RemovalPolicy.DESTROY
        })
    }

    public getRepository(){
        return this.repository
    }
}