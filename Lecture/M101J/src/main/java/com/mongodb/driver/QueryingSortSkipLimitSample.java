package com.mongodb.driver;


import static com.mongodb.Helpers.printJson;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
import static com.mongodb.client.model.Sorts.descending;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class QueryingSortSkipLimitSample {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("findWithSortSkipLimitTest");
		
		collection.drop();
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				collection.insertOne(new Document("i", i).append("j", j));
			}
		}
		
		
		Bson projection = fields( include("i", "j"), excludeId() );
		
		// Sorting methods
		//Bson sort = new Document("i", 1);
		//Bson sort = Sorts.ascending("i"); //static method
		//Bson sort = ascending("i");
		Bson sort = descending("j", "i");
		
		List<Document> all = collection.find()
					.projection(projection)
					.sort(sort)
					.skip(20) // skip first (X) rows
					.limit(50) // limit to (X) results
					.into(new ArrayList<Document>());
		for (Document document : all) {
			printJson(document);
		}
		
		client.close();

	}

}
