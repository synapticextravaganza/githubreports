/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved!
 */

package com.huntbach.github.domain;

/**
 * This class is this program's representation of a GitHub user. These
 * user objects are immutable once constructed.
 */
public class GitHubUser
{
	private String login;
	private String name;
	private String email;
	
	public GitHubUser( String login, String name, String email )
	{
		this.login = login;
		this.name = name;
		this.email = email;
	}
	
	/**
	 * Provide a JSON-like representation of this object instance.
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder( 128 );
		
		sb.append( "GitHubUser: { login: " ).append( this.login ).append( ", " )
			.append( "name: " ).append( this.name ).append( ", " )
			.append( "email: " ).append( this.email ).append( " }" );
		
		return sb.toString();
	}

	public String getLogin()
	{
		return this.login;
	}
	
	public String getName()
	{
		return name;
	}

	public String getEmail()
	{
		return email;
	}	
}
