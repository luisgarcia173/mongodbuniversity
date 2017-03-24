package com.mongodb;

import static spark.Spark.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import spark.utils.StringUtils;

public class SparkFormHandling {

	private SparkFormHandling() {
	}

	private static final Logger Log = Logger.getLogger("SparkFormHandling");
	
	public static void main(String[] args) {

		// Freemarker Configuration
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(SparkFormHandling.class, "/");
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		// Spark URLs
		get("/", (req, res) -> {
			// Freemarker Template
			StringWriter writer = new StringWriter();
			try {
				// Radio List
				Map<String, Object> fruitsMap = Maps.newHashMap();
				fruitsMap.put("fruits", Arrays.asList("Apple", "Orange", "Banana", "Peach"));
				
				// Template
				Template fruitTemplate = config.getTemplate("fruitPicker.ftl");

				fruitTemplate.process(fruitsMap, writer);
				Log.log(Level.INFO, writer.toString());
			} catch (IOException e) {
				halt(500);
				Log.log(Level.SEVERE, e.getMessage(), e);
			}
			return writer;
		});
		
		post("/favorite-fruit", (req, res) -> {
			final String fruit = req.queryParams("fruit");
			if (StringUtils.isEmpty(fruit)) {
				return "Why don't you pick one?";
			} else {
				return "Your favorite fruit is: " + fruit + "!";
			}
		});

	}

}
