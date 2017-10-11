/**
 * Copyright (c) David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.util.IOUtils;
import com.huntbach.githubreports.GithubreportsApplication;

/*
 * Application configuration object. Immutable once the Initialize method returns.
 */
@Configuration
public class ConfigStd implements Config
{
	private static Logger logger = Logger.getLogger( ConfigStd.class );
	
	/*
	 * Examples of Spring Externalized Configuration and Spring property value injection
	 */
	
	private static final String DEFAULT_EMAIL_FROM = "huntbach@comcast.net";
	@Value("${email_from:huntbach@comcast.net}")
	private String emailFrom = ConfigStd.DEFAULT_EMAIL_FROM;

	private static final String DEFAULT_EMAIL_SUBJECT = "GitHub Profile Name Missing";
	@Value("${email_subject:GitHub Profile Name Missing}")
	private String emailSubject = ConfigStd.DEFAULT_EMAIL_SUBJECT;

	/*
	 * frogger -- If there's time, read the e-mail bodies from files in src/main/resources.
	 */
	
	@SuppressWarnings( "unused" )
	private static final String EMAIL_HTML_BODY_FILENAME = "emailBody.html";
	private String emailHtmlBody =
			"<h1>Hi, so sorry to bother you but did you know that your name is missing in your \"{{login}}\" GitHub profile?</h1>"
			+ "<p>"
			+ "I'll take you to your profile if you'll just click <a href='https://github.com/settings/profile'>here</a>."
			+ "<p>"
			+ "<h3>Thank you very much!</h3>";
	
	@SuppressWarnings( "unused" )
	private static final String EMAIL_TEXT_BODY_FILENAME = "emailBody.txt";
	private String emailTextBody =
    	"Hi, so sorry to bother you but did you know that your name is missing in your \"{{login}}\" GitHub profile?"
    	+ "\n"
    	+ "You can go to your profile at https://github.com/settings/profile to add your name."
    	+ "\n"
    	+ "Thank you very much!";

	private ConfigGitHub gitHubConfig = null;
	private ConfigAWS awsConfig = null;
	
	/**
	 * Creates a configuration object that contains all configuration for the application.
	 */
	public ConfigStd()
	{
		this.gitHubConfig = new ConfigGitHub();
		this.awsConfig = new ConfigAWS();
	}
	
	/**
	 * Runs the command line through all command line processors.
	 * @return <code>true</code> on success; otherwise, if any of the processors fails, <code>false</code>.
	 */
	public boolean processCommandLineArguments( String...args ) throws IllegalArgumentException
	{
		boolean isSuccessful = true;
		
		isSuccessful &= this.gitHubConfig.processCommandLineArguments( args );
		isSuccessful &= this.awsConfig.processCommandLineArguments( args );
		
		return isSuccessful;
	}
	
	/**
	 * Calls <code>isValid()</code> on all configuration modules.
	 * @return <code>true</code> on success; otherwise, if any of the validatons fails, <code>false</code>.
	 */
	public boolean isValid()
	{
		boolean isSuccessful = true;

		isSuccessful &= this.gitHubConfig.isValid();
		isSuccessful &= this.awsConfig.isValid();
		
		return isSuccessful;
	}
	
	/**
	 * Reads the named file, assumed to be in the local directory, and returns
	 * <code>true</code> if successfully read and stored (using the provided 
	 * lambda function).
	 * @param filename - Name of the file from which to read.
	 * @param localFileContentsStorageLambda - Java 8 Lambda function used to store the contents 
	 * of the read file.
	 * @return <code>true</code> if successful; otherwise, <code>false</code>.
	 */
	@SuppressWarnings( "unused" )
	private boolean readFile( String filename, Consumer<String> localFileContentsStorageLambda )
	{
		boolean isSuccessful = true;

		try ( FileInputStream inputStream = new FileInputStream( filename ) ) 
		{     
			// use AWS IOUtils -- could also have used Apache IOUtils
	    String fileContents = IOUtils.toString( inputStream );
	    
	    // use lambda function to store file contents
	    localFileContentsStorageLambda.accept( fileContents );
		}
		catch( IOException ioE )
		{
			isSuccessful = false;
			logger.error( "Unable to read file=" + filename, ioE );
		}
		
		return isSuccessful;		
	}
	
	/**
	 * Prints application usage.
	 */
	protected static void usage()
	{
		System.out.format( "\n%s Usage:\n", GithubreportsApplication.class.getSimpleName() );
		System.out.println( "\t-h Print this usage help." );
		System.out.println( "\t-lLogin" );
		System.out.println( "\t-oOrganizationName" );
		System.out.println( "\t-pPassword (An OAuth Personal Access Token may also be used.)" );
	}	

	/*
	 * Getters & Setters
	 */
	
	public String getEmailFrom()
	{
		return emailFrom;
	}

	public String getEmailSubject()
	{
		return emailSubject;
	}

	public String getEmailHtmlBody()
	{
		return emailHtmlBody;
	}

	public String getEmailTextBody()
	{
		return emailTextBody;
	}

	public ConfigGitHub getGitHubConfig()
	{
		return gitHubConfig;
	}

	public ConfigAWS getAwsConfig()
	{
		return awsConfig;
	}
}
