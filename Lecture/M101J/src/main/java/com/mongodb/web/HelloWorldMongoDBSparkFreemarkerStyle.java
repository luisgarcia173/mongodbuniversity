package com.mongodb.web;

import static spark.Spark.get;
import static spark.Spark.halt;

import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class HelloWorldMongoDBSparkFreemarkerStyle {

	private HelloWorldMongoDBSparkFreemarkerStyle() {}
	
	private static final Logger Log = Logger.getLogger("HelloWorldSparkFreemarkerStyle");
	
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		// Freemarker Configuration
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(HelloWorldMongoDBSparkFreemarkerStyle.class, "/");
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		// MongoDB
		MongoClient client = new MongoClient();
		MongoDatabase db = client.getDatabase("course");
		final MongoCollection<Document> collection = db.getCollection("hello");
		collection.drop();
		
		// Insert a document
		collection.insertOne(new Document("name", "MongoDB"));
		
		// Spark URL
		get("/", (req, res) ->  {
			// Freemarker Template
			StringWriter writer = new StringWriter();
			try {
				Template helloTemplate = config.getTemplate("hello.ftl");
				Document document = collection.find().first();
				helloTemplate.process(document, writer);
				Log.log(Level.INFO, writer.toString());
			} catch (IOException | TemplateException e) {
				halt(500);
				Log.log(Level.SEVERE, e.getMessage(), e);
			}
			return writer;
		});
		
	}
	
}
