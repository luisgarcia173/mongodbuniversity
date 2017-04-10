package com.mongodb.morphia;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.mongodb.MongoClient;

public class MorphiaCRUD {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		Datastore datastore = setupMorphia();
		
		GithubUser user = new GithubUser();
		
		// Saving
		datastore.save(user);
		
		// Querying
		Query<GithubUser> query = datastore.createQuery(GithubUser.class);
		List<GithubUser> users = query.asList();
		List<GithubUser> usersWithFollowers = query.field("followers").lessThanOrEq(1000).asList();
		List<GithubUser> usersWithFilter = query.filter("followers <=", 1000).asList();
		
		// Updates
		final Query<GithubUser> queryFilter = query.filter("following <=", 5000);
		final UpdateOperations<GithubUser> inc = datastore.createUpdateOperations(GithubUser.class).inc("following", 500);
		datastore.update(queryFilter, inc);
		
		// Removes
		datastore.delete(queryFilter);
	}
	
	private static Datastore setupMorphia() {
		final Morphia morphia = new Morphia();

		// tell Morphia where to find your classes
		// can be called multiple times with different packages or classes
		morphia.mapPackage("org.mongodb.morphia.example");

		// create the Datastore connecting to the default port on the local host
		final Datastore datastore = morphia.createDatastore(new MongoClient(), "morphia_example");
		datastore.ensureIndexes();
		
		return datastore;
	}

}
