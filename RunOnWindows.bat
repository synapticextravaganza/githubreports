@echo off
REM *************************************************************************
REM * Script to help run githubreports on Windows from the "githubreports" directory.
REM *
REM * Don't forget to create the "oitskilleval" S3 bucket before running.
REM *
REM *************************************************************************

set AWS_ACCESS_KEY_ID=<your AWS access key id>
set AWS_SECRET_ACCESS_KEY=<your AWS secret access key>
set AWS_REGION=<us-east-1|us-west-2|eu-west-1>

java -jar .\target\githubreports-1.0.0-SNAPSHOT.jar -l<GitHubLogin> -o<GitHubOrganization> -p<GitHubPassword or Personal Access Token>
