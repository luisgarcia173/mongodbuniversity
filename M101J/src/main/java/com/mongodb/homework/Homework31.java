package com.mongodb.homework;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bson.Document;

import com.mongodb.Helpers;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

public class Homework31 {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("school");
		MongoCollection<Document> collection = db.getCollection("students");
		
		drop(collection);
		count(200, collection);
		
		deleteByMinScore(collection);
		count(200, collection);
		
		client.close();

	}
	
	@SuppressWarnings("unchecked")
	private static void deleteByMinScore(MongoCollection<Document> collection) {
		
		FindIterable<Document> students = collection.find(Filters.eq("scores.type", "homework"))
			.sort(Sorts.ascending(Arrays.asList("_id", "scores.scores")));
		
		Map<Integer, Double> studentsMap = new HashMap<>();
		for (Document student : students) {
			for (Document score : (List<Document>) student.get("scores")) {
				if ("homework".equalsIgnoreCase(score.getString("type"))) {
					if (studentsMap.get(student.getInteger("_id")) != null) {
						if (studentsMap.get(student.getInteger("_id")) > score.getDouble("score")) {
							studentsMap.put(student.getInteger("_id"), score.getDouble("score"));
						}
					} else {
						studentsMap.put(student.getInteger("_id"), score.getDouble("score"));
					}
				}
			}
		}
		
		Set<Entry<Integer, Double>> entrySet = studentsMap.entrySet();
		for (Entry<Integer, Double> entry : entrySet) {
			
			Document query = new Document().append("_id", entry.getKey());
			Document fields = new Document().append("scores", new Document().append( "type", "homework").append("score", entry.getValue()));
			Document update = new Document("$pull",fields);
			collection.updateOne(query, update);
			
			Helpers.printJson(collection.find(Filters.eq("_id", entry.getKey())).first());
		}
		
	}
	
	private static void drop(MongoCollection<Document> collection) {
		collection.drop();
		importData(collection);
	}
	
	private static void importData(MongoCollection<Document> collection) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("students.json");
        List<Document> documents = new ArrayList<>();
        String line;
        InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader br = new BufferedReader(isr);
        try {
			while ((line = br.readLine()) != null) {
			    documents.add(Document.parse(line));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        collection.insertMany(documents);
	}
	
	private static void count(int expected, MongoCollection<Document> collection) {
		System.out.println("Count ("+expected+"): " + collection.count());
	}

}
