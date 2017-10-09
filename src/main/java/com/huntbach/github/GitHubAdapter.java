/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 */

package com.huntbach.github;

import java.util.List;

import com.huntbach.github.domain.GitHubUser;

/**
 * This interface defines the contract for GitHub access. Different implementations
 * are envisioned for different GitHub libraries.
 */
public interface GitHubAdapter
{
	public List<GitHubUser> listOrganizationMembersWithoutNames( String gitHubOrgName );
}
