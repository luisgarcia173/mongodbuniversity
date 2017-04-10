package com.mongodb.driver;


import static com.mongodb.Helpers.printJson;
import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class DeleteSample {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("deleteTest");
		
		collection.drop();
		
		for (int i = 0; i < 10; i++) {
			collection.insertOne(new Document("_id", i));
		}
		
		collection.deleteOne(eq("_id", 4));
		collection.deleteMany(gt("_id", 4));
		
		for (Document document : collection.find().into(new ArrayList<Document>())) {
			printJson(document);
		}
		
		client.close();

	}

}
