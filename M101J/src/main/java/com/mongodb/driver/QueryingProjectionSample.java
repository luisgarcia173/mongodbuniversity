package com.mongodb.driver;


import static com.mongodb.Helpers.*;
import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Projections.*;

public class QueryingProjectionSample {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("findWithProjectionTest");
		
		collection.drop();
		
		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document()
					.append("x", new Random().nextInt(2))
					.append("y", new Random().nextInt(100))
					.append("i", i));
		}
		
		Bson filter = and(eq("x", 0), gt("y", 10), lt("y", 90));
		
		// Exclude fields from result, using Document
		//Bson projection = new Document("x", 0).append("_id", 0);
		
		// Exclude fields from result, using Projections
		//Bson projection = Projections.exclude("x", "_id");
		//Bson projection = Projections.include("x", "y"); //"_id" + declared fields in include(...)
		
		// Exclude fields from result, combining projections
		Bson projection = fields( include("y", "i"), excludeId() );
		
		List<Document> all = collection.find(filter)
					.projection(projection)
					.into(new ArrayList<Document>());
		for (Document document : all) {
			printJson(document);
		}
		
		client.close();

	}

}
