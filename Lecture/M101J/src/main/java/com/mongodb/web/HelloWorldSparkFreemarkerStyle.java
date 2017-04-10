package com.mongodb.web;

import static spark.Spark.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

public class HelloWorldSparkFreemarkerStyle {

	private HelloWorldSparkFreemarkerStyle() {}
	
	private static final Logger Log = Logger.getLogger("HelloWorldSparkFreemarkerStyle");
	
	public static void main(String[] args) {
		
		// Freemarker Configuration
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(HelloWorldSparkFreemarkerStyle.class, "/");
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		// Spark URL
		get("/", (req, res) ->  {
			// Freemarker Template
			StringWriter writer = new StringWriter();
			try {
				Template helloTemplate = config.getTemplate("hello.ftl");
				Map<String, Object> helloMap = Maps.newHashMap();
				helloMap.put("name", "Luis Garcia");
				
				helloTemplate.process(helloMap, writer);
				Log.log(Level.INFO, writer.toString());
			} catch (IOException | TemplateException e) {
				halt(500);
				Log.log(Level.SEVERE, e.getMessage(), e);
			}
			return writer;
		});
		
	}
	
}
