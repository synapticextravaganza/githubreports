/**
 * Copyright (c) David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.githubreports;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.util.IOUtils;

/*
 * Application configuration object. Immutable once the Initialize method returns.
 */
@Configuration
public class Config
{
	private static Logger logger = Logger.getLogger( Config.class );
	
	/*
	 * Examples of Spring Externalized Configuration and Spring property value injection
	 */
	
	@Value("${email_from}")
	private String email_from;
	
	@Value("${email_subject}")
	private String email_subject;
	
	/*
	 * frogger -- If there's time read the e-mail bodies from files in src/main/resources.
	 */
	
	@SuppressWarnings( "unused" )
	private static final String EMAIL_HTML_BODY_FILENAME = "emailBody.html";
	private String emailHtmlBody;
	
	@SuppressWarnings( "unused" )
	private static final String EMAIL_TEXT_BODY_FILENAME = "emailBody.txt";
	private String emailTextBody;

	private GitHubConfig gitHubConfig = null;
	
	/**
	 * Creates a configuration object that contains all configuration for the application.
	 */
	public Config()
	{
		this.gitHubConfig = new GitHubConfig();
	}
	
	/**
	 * Initializes this Config object from values found in the following order:
	 * <ol>
	 * <li><code>application.properties</code> file
	 * <li>environment variables
	 * <li>command line.
	 * </ol>
	 * Hence, command line values supersede any other values.
	 * @return <code>true</code> on success; otherwise, <code>false</code>.
	 */
	public boolean initialize( String...args )
	{
		if( this.gitHubConfig.processCommandLineArguments( args ) == false )
		{
			return false;
		}
		
		if( this.gitHubConfig.isValid() == false )
		{
			return false;
		}
		
		this.emailHtmlBody = 
				"<h1>Hi, so sorry to bother you but did you know that your name is missing in your \"{{login}}\" GitHub profile?</h1>"
				+ "<p>"
				+ "'ll take you to your profile if you'll just click <a href='https://github.com/settings/profile'>here</a>."
				+ "<p>"
				+ "<h3>Thank you very much!</h3>";
		
		this.emailTextBody = 
				"Hi, so sorry to bother you but did you know that your name is missing in your \"{{login}}\" GitHub profile?"
				+ "\n"
				+ "You can go to your profile at https://github.com/settings/profile to add your name."
				+ "\n"
				+ "Thank you very much!";
		
		return true;
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
		return email_from;
	}

	public String getEmailSubject()
	{
		return email_subject;
	}

	public String getEmailHtmlBody()
	{
		return emailHtmlBody;
	}

	public String getEmailTextBody()
	{
		return emailTextBody;
	}

	public GitHubConfig getGitHubConfig()
	{
		return gitHubConfig;
	}
}