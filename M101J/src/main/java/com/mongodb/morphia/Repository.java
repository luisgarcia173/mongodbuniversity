package com.mongodb.morphia;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import lombok.Data;

@Entity("repos")
public @Data class Repository {

	@Id
	private String name;
	@Reference
	private Organization organization;
	@Reference
	private GithubUser owner;
	private Settings settings = new Settings();
	
}
