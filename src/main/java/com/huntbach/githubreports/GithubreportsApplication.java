/**
 * Copyright (c) 2017, David J. Huntbach, All Rights Reserved.
 * 
 * Someparts are copyrighted and licensed by the following authors/licensors:
 * 
 * frogger -- add library licenses and copyrights
 */
package com.huntbach.githubreports;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GithubreportsApplication {

	public static void main(String[] args) {
		System.out.println("Happy Birthday Dave!");
		SpringApplication.run(GithubreportsApplication.class, args);
	}
}
