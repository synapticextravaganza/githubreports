/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 * 
 * Some parts of this application are copyrighted and licensed by the
 * following authors/licensors:
 * 
 * frogger -- add library licenses and copyrights
 */

package com.huntbach.githubreports;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.huntbach.github.GitHubAdapterKohsuke;
import com.huntbach.github.domain.GitHubUser;

import gnu.getopt.Getopt;

@SpringBootApplication
public class GithubreportsApplication implements CommandLineRunner
{
	private static Logger logger = Logger.getLogger( GithubreportsApplication.class );

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
		 * Validate arguments
		 */

		GitHubConfig gitHubConfig = GithubreportsApplication.processCommandLineArguments( args );
		if( gitHubConfig.getExitCode() != 0 )
		{
			System.exit( gitHubConfig.getExitCode() );
		}

		/*
		 * Get ALL (public and private) GitHub members who don't have a name set in their profiles
		 */

		GitHubAdapterKohsuke gitHubAdapter = 
				new GitHubAdapterKohsuke( 
						gitHubConfig.getGitHubLogin(), 
						gitHubConfig.getGitHubPassword() );
		
		List<GitHubUser> gitHubOrgMembersWithoutNames = 
				gitHubAdapter.listOrganizationMembersWithoutNames( gitHubConfig.getGitHubOrganization() );
		
		/*
		 * Send e-mails to GitHub members without names set in their profiles.
		 */
		
		for( GitHubUser user : gitHubOrgMembersWithoutNames )
		{
			
		}
		
		/*
		 * Save the list of GitHub members without names set in their profiles to an AWS S3 bucket.
		 * 
		 * NOTE: If the list were expected to be large, I would execute these loops in parallel. 
		 * 
		 */
		
		
	}

	/**
	 * Prints application usage.
	 */
	private static void usage()
	{
		System.out.format( "\n%s Usage:\n", GithubreportsApplication.class.getSimpleName() );
		System.out.println( "\t-lLogin" );
		System.out.println( "\t-oOrganizationName" );
		System.out.println( "\t-pPassword (An OAuth Personal Access Token may also be used.)" );
	}

	
	/**
	 * Processes command line arguments.
	 * @param args - Command line arguments varargs.
	 * @return 0 on success; any other value indicates that the application must exit 
	 * using the returned value as the application's exit code.
	 * <ul>
	 * <li>-1	Usage help requested.
	 * <li>-2 Missing GitHub login value.
	 * <li>-3 Missing GitHub organization value.
	 * <li>-4 Missing GitHub password or personal access token value.
	 * <li>-5 All parameters are missing.
	 * </ul>
	 * @throws IllegalArgumentException If <code>args</code> parameter is <code>null</code>.
	 */
	protected static GitHubConfig processCommandLineArguments( String... args ) throws IllegalArgumentException
	{
		GitHubConfig config = new GitHubConfig();

		if( args == null )
		{
			// All three command line parameters are required
			GithubreportsApplication.usage();
			config.setExitCode( GitHubConfig.EXIT_CODE_NO_PARAMETERS );
			return config;
		}
		
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
		Getopt g = new Getopt( GithubreportsApplication.class.getSimpleName(), args, "l:o:p:h" );
		int c;

		while( ( c = g.getopt() ) != -1 )
		{
			switch( c )
			{
			// Help and usage
			case 'h':
				GithubreportsApplication.usage();
				config.setExitCode( GitHubConfig.EXIT_CODE_HELP ); //With usage we just want to print it out and quit.
				break;
				
			case 'l':
				String login = g.getOptarg();
				if( login != null )
				{
					config.setGitHubLogin( login );
				}
				else
				{
					config.setExitCode( GitHubConfig.EXIT_CODE_MISSING_GITHUB_LOGIN );
				}
				break;
				
			case 'o':
				String gitHubOrganization = g.getOptarg();
				if( gitHubOrganization != null )
				{
					config.setGitHubOrganization( gitHubOrganization );
				}
				else
				{
					config.setExitCode( GitHubConfig.EXIT_CODE_MISSING_GITHUB_ORGANIZATION );
				}
				break;
				
			case 'p':
				String gitHubPassword = g.getOptarg();
				if( gitHubPassword != null )
				{
					config.setGitHubPassword( gitHubPassword );
				}
				else
				{
					config.setExitCode( GitHubConfig.EXIT_CODE_MISSING_GITHUB_PASSWORD );
				}
				break;

			default:
				String msg = "Invalid command line option=\"" + c + "\"";
				System.err.println( msg );
				logger.error( msg );
				config.setExitCode( GitHubConfig.EXIT_CODE_HELP );
				break;
			}
		}
		
		return config;
	}
	
	/**
	 * This class contains GitHub configuration parameters.
	 * 
	 * @author HuntbachDJ
	 */
	private static class GitHubConfig
	{
		public static final int EXIT_CODE_SUCCESS = 0;
		public static final int EXIT_CODE_HELP = -1;
		public static final int EXIT_CODE_MISSING_GITHUB_LOGIN = -2;
		public static final int EXIT_CODE_MISSING_GITHUB_ORGANIZATION = -3;
		public static final int EXIT_CODE_MISSING_GITHUB_PASSWORD = -4;
		public static final int EXIT_CODE_NO_PARAMETERS = -5;

		private int exitCode = GitHubConfig.EXIT_CODE_SUCCESS;
		private String gitHubOrganization = null;
		private String gitHubLogin = null;
		private String gitHubPassword = null;
		
		public int getExitCode()
		{
			return exitCode;
		}
		public void setExitCode( int exitCode )
		{
			this.exitCode = exitCode;
		}
		public String getGitHubOrganization()
		{
			return gitHubOrganization;
		}
		public void setGitHubOrganization( String gitHubOrganization )
		{
			this.gitHubOrganization = gitHubOrganization;
		}
		public String getGitHubLogin()
		{
			return gitHubLogin;
		}
		public void setGitHubLogin( String gitHubLogin )
		{
			this.gitHubLogin = gitHubLogin;
		}
		public String getGitHubPassword()
		{
			return gitHubPassword;
		}
		public void setGitHubPassword( String gitHubPassword )
		{
			this.gitHubPassword = gitHubPassword;
		}		
	}
}
