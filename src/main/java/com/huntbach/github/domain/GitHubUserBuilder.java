/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.github.domain;

/**
 * Creates GitHubUser instances using a Java Builder pattern.
 */
public class GitHubUserBuilder
{
	// Note: The member vars are protected to allow unit tests to access them.
	protected String login;
	protected String name;
	protected String email;
	
	public GitHubUserBuilder setLogin( String gitHubUserLogin )
	{
		this.login = gitHubUserLogin;
		return this;
	}
	
	public GitHubUserBuilder setName( String gitHubUserName )
	{
		this.name = gitHubUserName;
		return this;
	}
	
	public GitHubUserBuilder setEmail( String gitHubUserEmail )
	{
		this.email = gitHubUserEmail;
		return this;
	}
	
	public GitHubUser build()
	{
		return new GitHubUser( this.login, this.name, this.email );
	}
}

