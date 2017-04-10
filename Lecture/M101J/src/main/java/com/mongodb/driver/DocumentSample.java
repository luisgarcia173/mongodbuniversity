package com.mongodb.driver;

import java.util.Arrays;
import java.util.Date;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.Helpers.*;

public class DocumentSample {

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		Document document = new Document()
					.append("str", "MongoDB Hello")
					.append("int", 42)
					.append("long", 140l)
					.append("double", 18.5d)
					.append("bool", true)
					.append("date", new Date())
					.append("objectId", new ObjectId())
					.append("null", null)
					.append("embeddedDoc", new Document("x", 0))
					.append("list", Arrays.asList(1,2,3));
		
		printJson(document);
		
		BsonDocument bsonDocument = new BsonDocument("str", new BsonString("MongoDB Hello"));
		
	}

}
