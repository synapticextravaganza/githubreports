/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 * 
 * Kohsuke GitHub Library 
 * Copyright (c) 2010, Kohsuke Kawaguchi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.huntbach.githubreports;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.huntbach.config.ConfigStd;
import com.huntbach.email.EmailClient;
import com.huntbach.email.EmailClientAWS;
import com.huntbach.github.GitHubAdapter;
import com.huntbach.github.GitHubAdapterKohsuke;
import com.huntbach.github.domain.GitHubUser;
import com.huntbach.storage.StorageClient;
import com.huntbach.storage.StorageClientS3;

@SpringBootApplication
public class GithubreportsApplication implements CommandLineRunner
{
	private static Logger logger = Logger.getLogger( GithubreportsApplication.class );

	// If more time, let Spring wire up the configuration with @Autowired
	ConfigStd config = new ConfigStd();
	
	/**
	 * SpringBoot boilerplate main.
	 * @param args
	 */
	public static void main( String[] args )
	{
		SpringApplication.run( GithubreportsApplication.class, args );
	}

	/**
	 * The application code starts execution here. 
	 */
	@Override
	public void run( String... args ) throws Exception
	{
		/*
		 * Determine application configuration.
		 */

		if( config.processCommandLineArguments( args ) == false )
		{
			// configuration error
			// errors already logged so just exit
			System.exit( -1 );
		}

		/*
		 * Get ALL (public and private) GitHub members who don't have a name set in their profiles
		 */

		GitHubAdapter gitHubAdapter = new GitHubAdapterKohsuke( config.getGitHubConfig().getGitHubLogin(), config.getGitHubConfig().getGitHubPassword() );
		
		List<GitHubUser> gitHubOrgMembersWithoutNames = 
				gitHubAdapter.listOrganizationMembersWithoutNames( config.getGitHubConfig().getGitHubOrganization() );
		
		/*
		 * Send e-mails to GitHub members who didn't have names set in their profiles but that do 
		 * have a public e-mail address in their profiles.
		 */
		
		// this string will be written to AWS S3 -- NOTE: If possible for the contents to 
		// grow to more than 2 GB, the data must be written in 2G, or smaller, chunks since 
		// that's the maximum java String size
		StringBuilder namelessUsersLog = new StringBuilder();
		EmailClient emailClient = new EmailClientAWS();
		
		GitHubUserLoop:
		for( GitHubUser user : gitHubOrgMembersWithoutNames )
		{
			namelessUsersLog.append( String.format( "%s missing profile name. ", user.getLogin() ) );

			// does this user have a public e-mail address?
			if( user.getEmail() != null )
			{
				// yes, user has public e-mail address
  			
  			// replace login tags in e-mail message with actual user login 
  			String htmlBody = emailClient.replaceTemplateText( config.getEmailHtmlBody(), EmailClient.TEMPLATE_TAG_LOGIN, user.getLogin() );
  			String textBody = emailClient.replaceTemplateText( config.getEmailTextBody(), EmailClient.TEMPLATE_TAG_LOGIN, user.getLogin() );
  			
  			if( emailClient.send( config.getEmailFrom(), user.getEmail(), config.getEmailSubject(), htmlBody, textBody ) )
  			{
  				namelessUsersLog.append( String.format( "Email sent to %s.", user.getEmail() ) );
  			}
  			else
  			{
  				namelessUsersLog.append( "Email FAILED." ); 
  			}

  			// frogger -- While my AWS SES account is sandboxed I can only send, at most, one e-mail per second. Remove this sleep in production. 
  			try { Thread.sleep( 1200 ); } catch( InterruptedException iE )	{
  				logger.warn( "E-mail send throttling sleep interrupted.", iE );
  				break GitHubUserLoop; // exit GitHubUserLoop
  			}
			}
			else
			{
				namelessUsersLog.append( "No public e-mail address." );
			}
			namelessUsersLog.append( System.lineSeparator() );
		}
		
		/*
		 * Save the list of GitHub members without names set in their profiles to an object in an AWS S3 bucket.
		 * 
		 * NOTE: If the list were expected to be large, I would execute the e-mail and S3 loops in parallel. 
		 * 
		 */

		// are there nameless users to store?
		if( namelessUsersLog.length() > 0 )
		{
			logger.info( String.format( "Writing nameless users to AWS S3 bucket=\"%s\" and file (S3 key)=\"%s\".", config.getAwsConfig().getBucketName(), config.getAwsConfig().getFileName() ) );
			StorageClient storageClient = new StorageClientS3();
			if( storageClient.write( config.getAwsConfig().getBucketName(), config.getAwsConfig().getFileName(), namelessUsersLog.toString() ) == false )
  		{
  			logger.error( "Unable to write nameless users to AWS S3 bucket." );
  		}
		}
		else
		{
			logger.info( "No nameless users to write to AWS S3 bucket." );
		}
	}
}