/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.storage;

import org.apache.log4j.Logger;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Implements StorageClient interface to provide storage access to Amazon Web Services (AWS)
 * S3 storage. 
 */
public class StorageClientS3 implements StorageClient
{
	private static Logger logger = Logger.getLogger( StorageClientS3.class );
	
	private AmazonS3 s3Client;
	
	/**
	 * Creates a default AWS S3 client.
	 */
	public StorageClientS3()
	{
		this.s3Client = AmazonS3ClientBuilder.defaultClient();
	}
	
	/**
	 * Creates, or recreates (overwrites), a file and writes the given file contents to it. The contents are UTF-8 encoded.
	 * <p>
	 * <b>
	 * If this were a production system all calls to AWS would retry HTTP Status Codes and Java exceptions:
	 * <p>
	 * Retriable HTTP Status Codes
	 * <ul>
	 * <li>500 - Internal Server Error
	 * <li>502 - Bad Gateway
	 * <li>503 - Service Unavailable
	 * <li>504 - Gateway Timeout
	 * <li>408 - Request Timeout
	 * </ul>
	 * <p>
	 * Retriable Java Exceptions
	 * <ul>
	 * <li>InterruptedIOException -- InterruptedIOException is parent of SocketTimeoutException, ConnectTimeoutException, and ConnectionPoolTimeoutException
	 * <li>SocketException
	 * <li>NoHttpResponseException
	 * <li>IllegalStateException
	 * <li>ProtocolException
	 * <li>IOException && exception.getMessage().equalsIgnoreCase("Stream closed"))
	 * </ul>
	 * <p>
	 * In addition, the exceptions thrown by the Amazon S3 client would also be analyzed for a determination whether
	 * they represented a retriable error or not.
	 * </b>
	 * @param bucketName - S3 bucket name.
	 * @param filename - S3 object name or "key".
	 * @param completeFileContents - String containing the entire file to be written.
	 * @return <code>true</code> if write is successful; otherwise, <code>false</code>.
	 */
	@Override
	public boolean write( String bucketName, String filename, String completeFileContents )
	{
		try
		{
			this.s3Client.putObject( bucketName, filename, completeFileContents );
		}
		catch( SdkClientException e )
		{
			logger.error( "Unable to write to AWS S3 bucket.", e );
			return false;
		}
		return true;
	}
}