/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.config;

/**
 * AWS Configuration.
 */
public class ConfigAWS implements Config
{	
	/**
	 * Default name of the S3 bucket in which the nameless user file is stored.
	 */
	public static final String AWS_S3_BUCKET_NAME = "oitskilleval";
	private String bucketName = AWS_S3_BUCKET_NAME;
	
	/*
	 * Default name of the file (S3 key) in which the nameless user file is stored.
	 */
	public static final String NAMELESS_USER_FILENAME = "NamelessUsers.txt";
	private String fileName = NAMELESS_USER_FILENAME;
	
	@Override
	public boolean processCommandLineArguments( String... args ) throws IllegalArgumentException
	{
		// not yet any command line arguments for AWS
		return true;
	}
	
	public String getBucketName()
	{
		return bucketName;
	}

	public String getFileName()
	{
		return fileName;
	}
}
