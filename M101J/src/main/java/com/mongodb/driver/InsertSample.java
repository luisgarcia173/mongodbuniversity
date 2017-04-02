package com.mongodb.driver;


import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.Helpers.*;

import java.util.Arrays;

public class InsertSample {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		MongoCollection<Document> collection = db.getCollection("insertTest");
		
		collection.drop();
		
		Document smith = new Document("name", "Smith")
				.append("age", 30)
				.append("profession", "programmer");
		
		Document jones = new Document("name", "Jones")
				.append("age", 25)
				.append("profession", "designer");
		
		//printJson(smith); //Without ObjectId
		//collection.insertOne(smith);
		collection.insertMany(Arrays.asList(smith, jones));
		printJson(smith); //After insert, with ObjectId
		printJson(jones);
		
		client.close();

	}

}
