/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.config;

import org.apache.log4j.Logger;

import gnu.getopt.Getopt;

/**
 * This class manages GitHub configuration properties.
 * This class is mostly immutable. It can only be changed by processCommandLineArguments.
 */
public class ConfigGitHub implements Config
{
	private static Logger logger = Logger.getLogger( ConfigGitHub.class );
	
	private String organization = null;
	private String login = null;
	private String password = null;
	
	/**
	 * Processes GitHub settings command line arguments.
	 * @param args - Command line arguments varargs.
	 * @return <code>true</code> on success; otherwise, <code>false</code>.
	 * @throws IllegalArgumentException If <code>args</code> parameter is <code>null</code>.
	 */
	public boolean processCommandLineArguments( String... args ) throws IllegalArgumentException
	{
		/*
		 * Validate parameter
		 */
		
		if( args == null )
		{
			throw new IllegalArgumentException( "\"args\" must not be null." );
		}
		
		boolean isSuccessful = true;
		
		/*
		 * Getopt optstring parameter syntax:
		 * 
		 * Each character represents a valid option. If the character is followed by a single colon,
		 * then that option has a required argument. If the character is followed by two colons,
		 * then that option has an argument that is not required. If the character is not followed
		 * by a colon it does not have an argument.
		 * 
		 * If the first character in the option string is a colon, for example ":abc::d", then getopt()
		 * will return a ':' instead of a '?' when it encounters an option with a missing required
		 * argument. This allows the caller to distinguish between invalid options and valid options
		 * that are simply incomplete.
		 */
		Getopt g = new Getopt( ConfigGitHub.class.getSimpleName(), args, "l:o:p:h" );
		int c;

		
		GetOptLoop:
		while( ( c = g.getopt() ) != -1 )
		{
			switch( c )
			{
			case 'h':
				ConfigStd.usage();
				isSuccessful = false;	// just exit once -h is encountered
				break GetOptLoop;

			case 'l':
				String login = g.getOptarg();
				if( login != null )
				{
					this.login = login;
				}
				else
				{
					logger.error( "Missing GitHub login (-l) value." ); 
					isSuccessful = false; 
				}
				break;
				
			case 'o':
				String gitHubOrganization = g.getOptarg();
				if( gitHubOrganization != null )
				{
					this.organization = gitHubOrganization;
				}
				else
				{
					logger.error( "Missing GitHub organization (-o) value." ); 
					isSuccessful = false;
				}
				break;
				
			case 'p':
				String gitHubPassword = g.getOptarg();
				if( gitHubPassword != null )
				{
					this.password = gitHubPassword;
				}
				else
				{
					logger.error( "Missing GitHub password or OAuth private access token (-p) value." ); 
					isSuccessful = false;
				}
				break;
			}
		}
		
		return isSuccessful;
	}	

	/**
	 * Determines whether the current configuration is valid.
	 * @return <code>true</code> if valid; otherwise, <code>false</code>.
	 */
	public boolean isValid()
	{
		boolean isConfigValid = true;
		
		if( this.login == null )
		{
			logger.error( "Missing required \"-l Login\" parameter." );
			isConfigValid = false;
		}
		
		if( this.organization == null )
		{
			logger.error( "Missing required \"-o Organization\" parameter." );
			isConfigValid = false;
		}
		
		if( this.password == null )
		{
			logger.error( "Missing required \"-p Password or Personal Access Token\" parameter." );
			isConfigValid = false;
		}
		
		return isConfigValid;
	}

	public String getGitHubOrganization()
	{
		return organization;
	}

	public String getGitHubLogin()
	{
		return login;
	}

	public String getGitHubPassword()
	{
		return password;
	}	
}
