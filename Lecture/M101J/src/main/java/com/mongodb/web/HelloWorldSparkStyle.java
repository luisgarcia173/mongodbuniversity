package com.mongodb.web;

import static spark.Spark.*;

public class HelloWorldSparkStyle {

	private HelloWorldSparkStyle() {}
	
	public static void main(String[] args) {
		
		get("/", (req, res) ->  "Hello World from Spark and Lambda!" );
		
	}
	
}
