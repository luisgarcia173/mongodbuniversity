package com.mongodb.morphia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.utils.IndexType;

import lombok.Data;

@Entity(value = "users", noClassnameStored = true)
@Indexes({
	@Index(fields = { 
		@Field(value = "username", type = IndexType.ASC), 
		@Field(value = "-followers", type = IndexType.DESC)
	}, options = @IndexOptions(name = "popular")),
	@Index(fields = { 
		@Field(value = "lastActive"),
	}, options = @IndexOptions(name = "idle", expireAfterSeconds = 1000000000))
})
public @Data class GithubUser {

	@Id
	private String userName;
	private String fullName;
	@Property("since")
	private Date memberSince; 
	private Date lastActive;
	@Reference(lazy = true)
	private List<Repository> repositories = new ArrayList<>();
	private int followers = 0;
	private int following = 0;

}
