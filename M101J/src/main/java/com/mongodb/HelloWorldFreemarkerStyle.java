package com.mongodb;

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

public class HelloWorldFreemarkerStyle {

	private HelloWorldFreemarkerStyle() {}
	
	private static final Logger Log = Logger.getLogger("HelloWorldFreemarkerStyle");
	
	public static void main(String[] args) {
		
		// Configuration
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(HelloWorldFreemarkerStyle.class, "/");
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		
		//Template
		try {
			Template helloTemplate = config.getTemplate("hello.ftl");
			StringWriter writer = new StringWriter();
			Map<String, Object> helloMap = Maps.newHashMap();
			helloMap.put("name", "Luis Garcia");
			
			helloTemplate.process(helloMap, writer);
			Log.log(Level.INFO, writer.toString());
		} catch (IOException | TemplateException e) {
			Log.log(Level.SEVERE, e.getMessage(), e);
		}
		
		
	}
	
}
