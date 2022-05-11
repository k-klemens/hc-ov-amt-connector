# Amazon Mechanical Turk Crowdsourcing Connector
This plugins allows publishing, monitoring and retrieving results of HITs fron AMT.

## Setup
This plugin uses Amazon's AWS SDK and thus requires to be configured correctly.
In short: the credentials for a Amazon AWS account need to be supplied in `~/.aws/credentials`.
Further, the account must be allowed to access Region `US_EAST_1` as this will be enforced by the plugin.
For a details information on how to setup the SDK library refer to [Setting up the AWS SDK for Java 2.x](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/setup.html#setup-overview).