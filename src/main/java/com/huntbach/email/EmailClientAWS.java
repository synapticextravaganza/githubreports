/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.email;

import org.apache.log4j.Logger;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

/**
 * This class provides an implementation of the EmailClient interface
 * for sending e-mail via the Amazon Web Services (AWS) Java SDK to 
 * Simple E-mail Service (SES).
 */
public class EmailClientAWS implements EmailClient
{
	// "protected" for unit testing
	protected static Logger logger = Logger.getLogger( EmailClientAWS.class );

	/**
	 * Immutable AWS SES client used with this instance.
	 * Initialized in the class constructor.
	 * Note: "protected" solely for unit testing.
	 */
	protected AmazonSimpleEmailService sesClient;
	
	/**
	 * Creates a AmazonSimpleEmailServiceClient initialized with standard defaults.
	 * This client pulls its "Access Key ID", "Secret Access Key", and "AWS Region"
	 * from corresponding environment variables:
	 * <ul>
	 * <li>AWS_ACCESS_KEY_ID
	 * <li>AWS_SECRET_ACCESS_KEY
	 * <li>AWS_REGION
	 * </ul>
	 * @throws IllegalArgumentException If one, or more, of the required environment 
	 * variables is not set, or contains invalid values. 
	 */
	public EmailClientAWS() throws IllegalArgumentException
	{
		try
		{
			this.sesClient = AmazonSimpleEmailServiceClientBuilder.standard().build();
		}
		catch( Exception e )
		{
			logger.error( "Missing or invalid AWS Simple Email Service (SES) configuration.", e );
		}
	}

	/**
	 * Sends an e-mail.
	 * @param from - Sender e-mail address, e.g., samiam@byu.edu.
	 * @param to - Recipient e-mail address.
	 * @param subject - E-mail subject.
	 * @param htmlBody - HTML formatted message body.
	 * @param textBody - Plain text formatted message body.
	 * @return <code>true</code> on success; otherwise, <code>false</code>.
	 */
	public boolean send( String from, String to, String subject, String htmlBody, String textBody ) // throws IOException
	{
		/*
		 * Validate parameters
		 */
		
		if( from == null )
		{
			throw new IllegalArgumentException( "\"from\" parameter may not be null." );
		}
		if( to == null )
		{
			throw new IllegalArgumentException( "\"to\" parameter may not be null." );
		}
		
		try
		{
			SendEmailRequest request = 
				new SendEmailRequest()
					.withSource( from )
					.withDestination( new Destination().withToAddresses( to ) )
					.withMessage( 
							new Message()
  							.withSubject( new Content().withCharset( "UTF-8" ).withData( subject ) )
  							.withBody( 
  									new Body()
  										.withHtml( new Content().withCharset( "UTF-8" ).withData( htmlBody ) )
  										.withText( new Content().withCharset( "UTF-8" ).withData( textBody ) ) 
  							)
					);
			
			// if there were more time, the email result should be evaluated and handled
			// SendEmailResult result = sesClient.sendEmail( request );
			sesClient.sendEmail( request );
		}
		catch( Exception e )		// normally I don't like to catch "naked" Exception, I'm rushing the writting of this code
		{
			logger.error( "Email could not be sent.", e );
			return false;
		}
		
		return true;
	}
}
