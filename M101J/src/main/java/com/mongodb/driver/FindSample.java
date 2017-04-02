package com.mongodb.driver;


import static com.mongodb.Helpers.printJson;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class FindSample {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("findTest");
		
		collection.drop();
		
		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document("x", i));
		}
		
		System.out.println("Find one: ");
		Document first = collection.find().first();
		printJson(first);
		
		System.out.println("Find all with into: ");
		List<Document> all = collection.find().into(new ArrayList<Document>());
		for (Document document : all) {
			printJson(document);
		}
		
		System.out.println("Find all with iteration: ");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document cur = cursor.next();
				printJson(cur);
			}
		} finally {
			cursor.close();
		}
		
		System.out.println("Count: " + collection.count());
		
		client.close();

	}

}
