# Amazon Mechanical Turk Crowdsourcing Connector
This plugin for the [hc-ov-core](https://github.com/k-klemens/hc-ov-core) allows publishing, monitoring and retrieving results of HITs from [Amazon Mechanical Turk](https://www.mturk.com/).
Note that this is a protoypical implementation.

## Configuration Parameters
To interact with MTurk following configuration parameters passed with `crowdsourcingConnectorPluginConfiguration` in the `VerificationTaskSpecification' must be provided:
- _SANDBOX_: `true` or `false` indicating whether the tasks shall be published to the MTurk Sandbox or not
- _Description_: see parameter _Description_ at [CreateHIT](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_CreateHITOperation.html#ApiReference_CreateHITOperation-request-parameters)
- _Reward_: see parameter _Reward_ at [CreateHIT](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_CreateHITOperation.html#ApiReference_CreateHITOperation-request-parameters)
- _AssignmentDurationInSeconds_: see parameter _AssignmentDurationInSeconds_ at [CreateHIT](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_CreateHITOperation.html#ApiReference_CreateHITOperation-request-parameters)
- _Keywords_: see parameter _Keywords_ at [CreateHIT](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_CreateHITOperation.html#ApiReference_CreateHITOperation-request-parameters)
- _LifetimeInSeconds_: see parameter _LifetimeInSeconds_ at [CreateHIT](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_CreateHITOperation.html#ApiReference_CreateHITOperation-request-parameters)
- _MaxAssignments_ see parameter _MaxAssignments_ at [CreateHIT](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_CreateHITOperation.html#ApiReference_CreateHITOperation-request-parameters)

To specify a custom qualification type two URI pointing to a [QuestionForm](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_QuestionFormDataStructureArticle.html) and [AnswerKey](https://docs.aws.amazon.com/AWSMechTurk/latest/AWSMturkAPI/ApiReference_AnswerKeyDataStructureArticle.html)
can be provided using the properties _QUALIFICATION_TEST_URI_ and _ANSWER_KEY_URI_
## Setup
This plugin uses Amazon's AWS SDK and thus requires to be configured correctly.
In short: the credentials for a Amazon AWS account need to be supplied in `~/.aws/credentials`.
Further, the account must be allowed to access Region `US_EAST_1` as this will be enforced by the plugin.
For a details information on how to setup the SDK library refer to [Setting up the AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup.html#setup-overview).

For more information about to include this plugin to the `hc-ov-core` refer to [hc-ov-sdk](https://github.com/k-klemens/hc-ov-sdk)