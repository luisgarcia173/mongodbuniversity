package com.mongodb.morphia;

import java.util.Date;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Version;

import lombok.Data;

@Entity("orgs")
@Indexes({
	@Index(fields = { 
	}, options = @IndexOptions(unique = false, background = false, sparse = false, expireAfterSeconds = -1))
})
public @Data class Organization {

	@Id
	private String name;
	private Date created;
	@Version("v")
	private long version;
	
}
