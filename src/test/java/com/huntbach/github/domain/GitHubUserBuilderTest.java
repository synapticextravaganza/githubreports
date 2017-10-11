/**
 * Copyright (c) David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.github.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class GitHubUserBuilderTest
{
	@Test
	public void testConstructor()
	{
		GitHubUserBuilder builder = new GitHubUserBuilder();
		assertNotNull( builder );
	}

	@Test
	public void testSetLogin()
	{
		GitHubUserBuilder builder = new GitHubUserBuilder();
		builder.setLogin( "gitHubUserLogin" );
		assertEquals( "gitHubUserLogin", builder.login );
	}
	
	@Test
	public void testSetName()
	{
		GitHubUserBuilder builder = new GitHubUserBuilder();
		builder.setName( "gitHubUserName" );
		assertEquals( "gitHubUserName", builder.name );
	}
	
	@Test
	public void testSetEmail()
	{
		GitHubUserBuilder builder = new GitHubUserBuilder();
		builder.setEmail( "gitHubUserEmail" );
		assertEquals( "gitHubUserEmail", builder.email );
	}
	
	@Test
	public void testBuild()
	{
		GitHubUserBuilder builder = new GitHubUserBuilder();
		builder.setLogin( "gitHubUserLogin" );
		builder.setName( "gitHubUserName" );
		builder.setEmail( "gitHubUserEmail" );
		
		GitHubUser user = builder.build();
		
		assertEquals( builder.login, user.getLogin() );
		assertEquals( builder.name, user.getName() );
		assertEquals( builder.email, user.getEmail() );
	}
}
