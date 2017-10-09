/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.github;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;

import com.huntbach.github.domain.GitHubUser;
import com.huntbach.github.domain.GitHubUserBuilder;

/**
 * This module contains methods used to interface with GitHub.
 */
public class GitHubAdapterKohsuke implements GitHubAdapter
{

	private static Logger logger = Logger.getLogger( GitHubAdapterKohsuke.class );

	/**
	 * Immutable reference to the root GitHub library object.
	 * Initialized in the class constructor.
	 */
	private GitHub gitHub;

	/**
	 * Creates an adapter for interaction with the GitHub API.
	 * @param gitHubLogin - GitHub user id.
	 * @param gitHubOauthToken - GitHub user password or personal access token.
	 * @throws IOException If unable to establish a connection to GitHub.
	 */
	public GitHubAdapterKohsuke( String gitHubLogin, String gitHubOauthToken ) throws IOException
	{

		try
		{
			this.gitHub = GitHub.connect( gitHubLogin, gitHubOauthToken );
		}
		catch( IOException ioE )
		{
			logger.error( "Unable to connect to GitHub using login=" + gitHubLogin + " and token=" + gitHubOauthToken );
			throw ioE;
		}
	}

	/**
	 * Retrieves the members associated with the given GitHub Organization who do not have names (null)
	 * in their user profiles.
	 * @param gitHubOrgName - Name of the GitHub organization for which members are being requested.
	 * @return 
	 * <ul>
	 * <li><code>null</code> if the members cannot be retrieved.
	 * <li>List of organization members sans names. Note that the list will be empty if there are no 
	 * accessible members.
	 * </ul>
	 * @throws IOException If the list of GitHub organization members cannot be retrieved.
	 */
	public List<GitHubUser> listOrganizationMembersWithoutNames( String gitHubOrgName )
	{
		List<GitHubUser> memberList = new ArrayList<GitHubUser>();
		try
		{
			GHOrganization ghOrg = this.gitHub.getOrganization( gitHubOrgName );
			PagedIterable<GHUser> members = ghOrg.listMembers();
			for( GHUser ghUser : members )
			{
				// does this user have a name?
				if( ghUser.getName() == null )
				{
  				// yes, so use Builder Pattern to create generic GitHub user objects
  				memberList.add( 
  						new GitHubUserBuilder()
  							.setLogin( ghUser.getLogin() )
  							.setName( ghUser.getName() )
  							.setEmail( ghUser.getEmail() )
  							.build() );
				}
			}
			return memberList;
		}
		catch( IOException ioE )
		{
			logger.error( "Unable to list GitHub organization members.", ioE );
			return null;
		}
	}
}
