/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.storage;

/**
 * This interface defines the contract through which storage libraries are accessed. 
 */
public interface StorageClient
{
	public boolean write( String path, String filename, String completeFileContents );
}
