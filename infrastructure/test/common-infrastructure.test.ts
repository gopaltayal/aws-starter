import * as cdk from 'aws-cdk-lib';
import * as CommonInfrastructure from '../lib/common-infrastructure-stack';
import {Template} from "aws-cdk-lib/assertions";

test('Common Infra creates ECR Repository', () => {
    const app = new cdk.App();
    // WHEN
    const stack = new CommonInfrastructure.CommonInfrastructureStack(app, 'MyTestStack');
    // THEN
    const template = Template.fromStack(stack);
    template.hasResourceProperties('AWS::ECR::Repository', {
        RepositoryName: "aws-starter-repo"
    });
});