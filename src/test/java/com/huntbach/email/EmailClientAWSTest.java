/**
 * Copyright (c), David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.email;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;

@RunWith(JMockit.class)
public class EmailClientAWSTest
{
	@Mocked AmazonSimpleEmailServiceClientBuilder mockedAmazonSimpleEmailServiceClientBuilder;
	@Mocked AmazonSimpleEmailService mockedSesClient;
	@Mocked Logger mockedLogger;
	
	@Test
	public void testConstructor()
	{
		EmailClientAWS emailClientAWS = null;
		
		new NonStrictExpectations()
		{
			{
				mockedAmazonSimpleEmailServiceClientBuilder.build();
				result = mockedSesClient;
			}
		};
		
		/*
		 * Test happy path where everything works as expected.
		 */
		
		emailClientAWS = new EmailClientAWS();
		assertNotNull( emailClientAWS );
		
		/*
		 * Test exception path.
		 */

		new StrictExpectations()
		{
			{
				mockedAmazonSimpleEmailServiceClientBuilder.build();
				result = new Exception( "dummy test exception" );
				
				// just verify that the exception was caught
				mockedLogger.error( (Object) any, (Throwable) any );
				result = null;
			}
		};
		
		emailClientAWS = new EmailClientAWS();		
	}
	
	@Test
	public void testSend()
	{
		boolean sendResult;
		
		new NonStrictExpectations()
		{
			{
				mockedAmazonSimpleEmailServiceClientBuilder.build();
				result = mockedSesClient;
			}
		};

		EmailClientAWS emailClientAWS = new EmailClientAWS();
		
		new StrictExpectations()
		{
			{
				mockedSesClient.sendEmail( (SendEmailRequest) any );
				result = new SendEmailResult(); 
			}
		};
		
		sendResult = emailClientAWS.send( "from", "to", "subject", "htmlBody", "textBody" );
		assertTrue( sendResult );
		// StrictExpectation verifies that the code was called correctly so no further
		// validation is necessary.
	}
	
	@Test
	public void testSendErrorCases()
	{
		boolean sendResult;

		new NonStrictExpectations()
		{
			{
				mockedAmazonSimpleEmailServiceClientBuilder.build();
				result = mockedSesClient;
			}
		};

		EmailClientAWS emailClientAWS = new EmailClientAWS();
		
		/*
		 * Test send throwing an exception. false will be returned.
		 */
		
		new NonStrictExpectations()
		{
			{
				mockedSesClient.sendEmail( (SendEmailRequest) any );
				result = new Exception( "dummy test exception" );
			}
		};
		
		sendResult = emailClientAWS.send( "from", "to", "subject", "htmlBody", "textBody" );
		assertFalse( sendResult );
		
		/*
		 * Test null "from" parameter. IllegalArgumentException should be thrown.
		 */
		
		try
		{
			sendResult = emailClientAWS.send( null, "to", "subject", "htmlBody", "textBody" );
			fail( "An exception should have been thrown." );
		}
		catch( IllegalArgumentException iaE )
		{
			// this is suppossed to happen
		}
		
		/*
		 * Test null "to" parameter. IllegalArgumentException should be thrown.
		 */
		
		try
		{
			sendResult = emailClientAWS.send( "from", null, "subject", "htmlBody", "textBody" );
			fail( "An exception should have been thrown." );
		}
		catch( IllegalArgumentException iaE )
		{
			// this is suppossed to happen
		}
	}
}
