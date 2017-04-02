package com.mongodb.homework;


import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.Helpers;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Homework25 {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("video");
		MongoCollection<Document> collection = db.getCollection("movieDetails");
		
		//Bson filter = Filters.and(Filters.eq("year", 2013), Filters.eq("rated", "PG-13"), Filters.eq("awards.wins", 0));
		Bson filter = Filters.eq("countries.1", "Sweden");
		FindIterable<Document> documents = collection.find(filter);
		int i = 0;
		for (Document document : documents) {
			Helpers.printJson(document);
			//i++;
		}
		System.out.println(i);
		client.close();

	}
	
}
