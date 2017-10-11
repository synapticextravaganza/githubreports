# Synaptic Extravaganza Presents: *githubreports*

*githubreports* is a Java 8 program that reads all user profiles (public and private) for a given GitHub organization and sends e-mails to organization members who don't have a public profile name, but who do have a public e-mail address. In addition, a file is written to an Amazon Web Services (AWS) S3 bucket containing the GitHub login of the nameless organization members. If a nameless member does not have a public e-mail address, no e-mail can be sent. No worries, even these secretive souls are identified in the file written to S3.

## How to Build *githubreports*

*githubreports* is a Maven project so,

1. Clone the synapticextravaganza GitHub repo from https://github.com/synapticextravaganza/githubreports to a local directory. 
2. In that local directory execute `mvn clean package`.

This will create an executable JAR file in the "target" directory just subordinate to the local directory. 

## How to Run *githubreports*

*githubreports* relies upon three required command line arguments for GitHub login (-l), organization (-o), and password (-p). Note that a Git Hub Personal User Access Token (PUAT) can be used in place of a password. Note that the GitHub login user must be a member of the given GitHub organization. 

*githubreports* also relies upon three required AWS environment variables, `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, and `AWS_REGION`. These variables provide configuration for both the AWS Simple Email Service (SES) and the Simple Storage Service (S3). The only other configuration necessary is to create an S3 bucket named, `oitskilleval` in the `AWS_REGION` and account associated with `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`. I would have made the S3 bucket name configurable and created the bucket dynamically but limited time didn't allow that convenience. Note that SES is only available in the following AWS regions:

* us-east-1 (N. Virginia)
* us-west-2 (Oregon)
* eu-west-1 (Ireland)

###### Sample AWS Environment Variable Values

* `AWS_ACCESS_KEY_ID`=ZPIAJ6ERPAQJFWPSLQCS
* `AWS_SECRET_ACCESS_KEY`=+VDPgpo5QSPAehd1m9cPnXTmSc7BZxQl
* `AWS_REGION`=us-west-2

Just in case you were wondering, none of the keys, secrets, or passwords in these examples are real. Change directory (cd) to the "target" directory and execute the following command to run the application:

`java -jar githubreports-1.0.0.jar -lferdfarkle -osynapticextravaganza -pa19356794d4basdfbb3139d1ae2b9agjd27b5d6h5b`

I've provided a script, **"RunOnWindows.bat"**, in the root "githubreports" directory of the project to make this setup and execution easier. 

## What's the S3 Object (file) Look Like?

The report is written to an S3 object (file) named, "NamelessUsers.txt", and will look substantially like the following. Note that there are three members in the synapticextravaganza organization that are nameless; however, only one of the three has a public e-mail address, "d1withemail@gmail.com".
```
ferdfarkle missing profile name. No public e-mail address.
huntbachdj missing profile name. No public e-mail address.
nonamesam missing profile name. Email sent to d1withemail@gmail.com.
```

## Features of *githubreports*

1. JUnit unit tests with JMockit mocking.
2. Interfaces for all libraries that could support multiple implementations.
3. Fluent builder pattern used where it made sense.
4. Lambda functions.
5. Interfaces with default methods.
6. Immutable data classes.
7. Uses AWS Simple Email Service in addition to S3.
8. Takes advantage of Open Source GitHub library (kohsuke).
9. Written in a very short time. I received the assignment Friday evening. Saturday was my birthday. I didn't work on it on the Sabbath. Monday I had a root canal. Tuesday and Wednesday I was attending a conference.

## Assumptions

* Will not work if GitHub Multi-Factor Authentication (MFA) is enabled.

