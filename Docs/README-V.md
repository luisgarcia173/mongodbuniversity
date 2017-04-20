### Week V (Aggregation) ###

** $group **

Grouping with SQL
```
#!sql
SELECT manufacturer, COUNT(*) FROM products GROUP BY manufactures;
```

Grouping with MongoDB
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: "$manufacturer", 
			num_products: {$sum: 1}
		}
	}
]);
```

** Pipelines **

Aggregation pipelines:

* $project: reshape (1:1)
* $match: filter (n:1)
* $group: aggregate (n:1)
* $sort: sort (1:1)
* $skip: skips (n:1)
* $limit: limits (n:1)
* $unwind: normalize (1:n)
* $out: output (1:1)

Location pipelines:

* $redact and $geonear

** Compound Grouping **

Example I:
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: {
				"maker": "$manufacturer",
				"category": "$category"
			}, 
			num_products: {$sum: 1}
		}
	}
]);
```

Example II:
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: {"$manufacturer", "$category"}, 
			num_products: {$sum: 1}
		}
	}
]);
```

** Aggregation Expressions **

Group:

* $sum: sum keys
* $avg: get the average of key values in aggregation stage
* $min: get the minimum of key values in aggregation stage
* $max: get the maximum of key values in aggregation stage
* $push: build array fo the result
* $addToSet: build array fo the result as unique keys
* $first: find the first in an aggregation query (sort)
* $last: find the last in an aggregation query (sort)

** $sum: ** to count or sum values

```
#!mongodb
db.zips.aggregate([{"$group":{"_id":"$state", "population":{$sum:"$pop"}}}]);
```

** $avg: ** average of values

```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: {"category": "$category"}, 
			avg_price: {$avg: "$price"}
		}
	}
]);
```

** $addToSet: ** create array to group values like SET in java, so you will not duplicity
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: {"maker": "$manufacturer"}, 
			categories: {$addToSet: "$category"}
		}
	}
]);
```

** $push: ** create array to group values so you might have duplicity
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: {"maker": "$manufacturer"}, 
			categories: {$push: "$category"}
		}
	}
]);
```

** $max and $min ** create array to group values so you might have duplicity
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: {"maker": "$manufacturer"}, 
			maxprice: {$max: "$price"}
		}
	}
]);
```

** double $group stages ** allow you to use multiple group funcions
```
#!mongodb
db.grades.aggregate([
	{$group: {_id: {"class_id": "$class_id", "student_id": "$student_id"}, average: {$avg: "$score"}}},
	{$group: {_id: "$_id.class_id", average: {$avg: "$average"}}}
]);
```

** $project ** reshape the document:

* remove keys
* add new keys
* reshape keys
* use some simple functions on keys ($toUpper, $toLower, $add, $multiply, ...)

Example:
```
#!mongodb
db.products.aggregate([
	{$project: 
		{
			_id: 0, //means you are removing the key _id
			maker: {$toLower: "$manufacturer"},
			details: {category: "$category", price: {$multiply: ["$price", 10]}},
			item: "$name"
		}
	}
]);
```

** $match ** filter for aggregation

* filter n:1
* pre aggregation filter
* filter teh results

Simple Example:
```
#!mongodb
db.zips.aggregate([{$match:{pop:{$gt:100000}}}]);
```

Complex Example:
```
#!mongodb
db.zips.aggregate([
	{$match: {state: "CA"}},
	{$group: 
		{
			_id: "$city", 
			population: {$sum: "$pop"},
			zip_codes: {$addToSet: "$_id"}
		}
	},
	{$project: 
		{
			_id: 0,
			city: "$_id",
			population: 1,
			zip_codes: 1
		}
	}
]);
```

** $sort **

* disk and memory based, 100MB
* before or after the grouping stage

Simple Example:
```
#!mongodb
db.zips.aggregate([{$sort:{state:1, city:1}}]);
```

Complex Example:
```
#!mongodb
db.zips.aggregate([
	{$match: {state: "NY"}},
	{$group: 
		{
			_id: "$city", 
			population: {$sum: "$pop"}
		}
	},
	{$project: 
		{
			_id: 0,
			city: "$_id",
			population: 1
		}
	},
	{$sort: {population: -1}}
]);
```

** $skip and $limit **

Should use it together in the following order: $skip -> $limit

Example:
```
#!mongodb
db.zips.aggregate([
	{$match: {state: "NY"}},
	{$group: 
		{
			_id: "$city", 
			population: {$sum: "$pop"}
		}
	},
	{$project: 
		{
			_id: 0,
			city: "$_id",
			population: 1
		}
	},
	{$sort: {population: -1}},
	{$skip: 10},
	{$limit: 5}
]);
```

** $first and $last **

```
#!mongodb
db.zips.aggregate([
	/* get the population of every city in every state */
	{$group:
		{
			_id: {state: "$state", city: "$city"},
			population: {$sum: "$pop"}
		}
	},
	/* sort by state, population*/
	{$sort: {"_id.state": 1, "population": -1}},
	/* group by state, get the first item in each group */
	{$group:
		{
			_id: "_id.state",
			city: {$first: "$_id.city"},
			population: {$first: "$population"}
		}
	}
]);
```

** $unwind **

Split arrays in the documents:
```
#!json
{ "_id" : "Will", "likes" : [ "physics", "MongoDB", "indexes" ] }
```

After *unwind*:
```
#!json
{ "_id" : "Will", "likes" : "physics" }
{ "_id" : "Will", "likes" : "MongoDB" }
{ "_id" : "Will", "likes" :"indexes" }
```

Query example:
```
#!mongodb
db.posts.aggregate([
	/* unwind by tags */
	{$unwind: "tags"},
	/* now group by tags, counting each tag */
	{$group:
		{
			_id: "$tags",
			count: {$sum: 1}
		}
	},
	/* sort by popularity*/
	{$sort: {"count": -1}},
	/* show me the top 10 */
	{$limit: 10},
	/* change the name of _id to be the tag*/
	{$project:
		{
			_id: 0,
			tag: "$_id",
			count: 1
		}
	}
]);
```

To revert the $unwind, you should use $push operator.