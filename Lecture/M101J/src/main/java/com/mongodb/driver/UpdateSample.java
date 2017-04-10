package com.mongodb.driver;


import static com.mongodb.Helpers.printJson;
import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

public class UpdateSample {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("updateTest");
		
		collection.drop();
		
		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document("_id", i)
									.append("x", i)
									.append("y", true));
		}
		
		// Replace
		collection.replaceOne(eq("x", 5), new Document("x", 55).append("updated", true));
		
		// Updade
		//collection.updateOne(eq("x", 7), new Document("$set", new Document("x", 77).append("updated", true)));
		collection.updateOne(eq("x", 7), Updates.combine(Updates.set("x", 77), Updates.set("updated", true)));
		
		// Upsert: If not exists, then insert it
		collection.updateOne(eq("_id", 10), 
							 Updates.combine(Updates.set("x", 100), Updates.set("updated", true)),
							 new UpdateOptions().upsert(true));
		
		//Update Many
		collection.updateMany(gte("x", 5), Updates.inc("x", 1));
		
		for (Document document : collection.find().into(new ArrayList<Document>())) {
			printJson(document);
		}
		
		client.close();

	}

}
