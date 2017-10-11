/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.config;

/**
 * This interface defines the contract fullfilled by all configuration modules.
 */
public interface Config
{
	public boolean processCommandLineArguments( String... args ) throws IllegalArgumentException;
	
	default public boolean isValid()
	{
		return true;
	}
}
