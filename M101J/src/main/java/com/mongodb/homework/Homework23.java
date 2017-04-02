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
import org.bson.conversions.Bson;

import com.mongodb.Helpers;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

public class Homework23 {

	public static void main(String[] args) {
		
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("students");
		MongoCollection<Document> collection = db.getCollection("grades");
		
		drop(collection);
		importData(collection);
		count(800, collection);
		
		//db.grades.aggregate({'$group':{'_id':'$student_id', 'average':{$avg:'$score'}}}, {'$sort':{'average':-1}}, {'$limit':1})
		groupByAverage(collection);

		//db.grades.find({'score':{$gte:65}}).sort({'score':1}).limit(1).pretty()
		findByScore(collection);
		
		//db.grades.aggregate({'$group':{'_id':'$student_id', 'score':{'$min':'$score'}}}, {'$sort':[{'_id':1}, {'score':1}]})
		deleteByMinScore(collection);
		count(600, collection);
		
		//db.grades.find().sort( { 'score' : -1 } ).skip( 100 ).limit( 1 )
		findSorted(collection);
		
		//db.grades.find( { }, { 'student_id' : 1, 'type' : 1, 'score' : 1, '_id' : 0 } ).sort( { 'student_id' : 1, 'score' : 1 } ).limit( 5 )
		listByProjection(collection);
		
		//db.grades.aggregate({'$group':{'_id':'$student_id', 'average':{$avg:'$score'}}}, {'$sort':{'average':-1}}, {'$limit':1})
		groupByAverage(collection);
		
		client.close();

	}
	
	private static void listByProjection(MongoCollection<Document> collection) {
		Bson projections = Projections.fields(Projections.include(Arrays.asList("student_id", "type", "score")),
					  						  Projections.excludeId());
		FindIterable<Document> findProjections = collection.find()
			.projection(projections)
			.sort(Sorts.ascending(Arrays.asList("student_id", "score")))
			.limit(5);
		System.out.println("Expected:");
		System.out.println(
			"{ 'student_id' : 0, 'type' : 'quiz', 'score' : 31.95004496742112 }\n"+
			"{ 'student_id' : 0, 'type' : 'exam', 'score' : 54.6535436362647 }\n"+
			"{ 'student_id' : 0, 'type' : 'homework', 'score' : 63.98402553675503 }\n"+
			"{ 'student_id' : 1, 'type' : 'homework', 'score' : 44.31667452616328 }\n"+
			"{ 'student_id' : 1, 'type' : 'exam', 'score' : 74.20010837299897 }"
		);
		System.out.println("Retrieved:");
		for (Document document : findProjections) {
			Helpers.printJson(document);
		}
	}
	
	private static void findSorted(MongoCollection<Document> collection) {
		Document findSkip100 = collection.find()
				.sort(Sorts.descending("score"))
				.skip(100)
				.first();
		System.out.println("Expected:");
		System.out.println("{ '_id' : ObjectId('50906d7fa3c412bb040eb709'), 'student_id' : 100, 'type' : 'homework', 'score' : 88.50425479139126 }");
		Helpers.printJson(findSkip100);
	}
	
	private static void deleteByMinScore(MongoCollection<Document> collection) {
//		Bson filter = Aggregates.match(Filters.eq("type", "homework"));
//		Bson group = Aggregates.group("$student_id", Accumulators.min("score", "$score"));
//		Bson sort = Aggregates.sort(Sorts.ascending(Arrays.asList("_id", "score")));
//		AggregateIterable<Document> groupByMinScore = collection.aggregate(Arrays.asList(filter, group, sort));
		//return groupByMinScore;
		
		FindIterable<Document> sort2 = collection.find(Filters.eq("type", "homework"))
			.sort(Sorts.ascending(Arrays.asList("student_id", "score")));
		
		Map<Integer, Double> students = new HashMap<>();
		for (Document document : sort2) {
			if (students.get(document.getInteger("student_id")) != null) {
				if (students.get(document.getInteger("student_id")) > document.getDouble("score")) {
					students.put(document.getInteger("student_id"), document.getDouble("score"));
				}
			} else {
				students.put(document.getInteger("student_id"), document.getDouble("score"));
			}
		}
		
		Set<Entry<Integer, Double>> entrySet = students.entrySet();
		for (Entry<Integer, Double> entry : entrySet) {
			Document doc = collection.find(Filters.and(Filters.eq("student_id", entry.getKey()), Filters.eq("score", entry.getValue()))).first();
			collection.deleteOne(Filters.eq("_id", doc.get("_id")));
		}
		
	}
	
	private static void findByScore(MongoCollection<Document> collection) {
		Bson filter = Filters.gte("score", 65);
		Document findByScore = collection.find(filter)
				.projection(Projections.fields(Projections.include("student_id"), Projections.excludeId()))
				.sort(Sorts.ascending("score"))
				.first();
		Helpers.printJson(findByScore);
	}
	
	private static void groupByAverage(MongoCollection<Document> collection) {
		Bson group = Aggregates.group("$student_id", Accumulators.avg("average", "$score"));
		Bson sort = Aggregates.sort(Sorts.descending("average"));
		Document groupByAverage = collection.aggregate(Arrays.asList(group, sort)).first();
		Helpers.printJson(groupByAverage);
	}
	
	private static void drop(MongoCollection<Document> collection) {
		collection.drop();
	}
	
	private static void importData(MongoCollection<Document> collection) {
		InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("grades.json");
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
