/**
 * Copyright (c), David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.github.domain;

import static org.junit.Assert.*;

import org.junit.Test;

public class GitHubUserTest
{
	@Test
	public void testConstructor()
	{
		GitHubUser user = new GitHubUser( "login", "name", "email" );
		assertNotNull( user );
	}
	
  @Test
  public void testToString()
  {
		GitHubUser user = new GitHubUser( "login", "name", "email" );
	
    assertEquals( "GitHubUser: { login: login, name: name, email: email }", user.toString() );
  }

  @Test
  public void testGetters()
  {
		GitHubUser user = new GitHubUser( "login", "name", "email" );

		assertEquals( "login", user.getLogin() );
		assertEquals( "name", user.getName() );
		assertEquals( "email", user.getEmail() );
  }
}
