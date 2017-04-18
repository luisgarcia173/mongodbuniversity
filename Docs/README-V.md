### Week V (Aggregation) ###

** $group **

Grouping with SQL
```
#!sql
SELECT manufactures, COUNT(*) FROM products GROUP BY manufactures;
```

Grouping with MongoDB
```
#!mongodb
db.products.aggregate([
	{$group: 
		{
			_id: "$manufactures", 
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
				"maker": "$manufactures",
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
			_id: {"$manufactures", "$category"}, 
			num_products: {$sum: 1}
		}
	}
]);
```

** Aggregation Expressions **

Group
* $sum: sum keys
* $avg: get the average of key values in aggregation stage
* $min: get the minimum of key values in aggregation stage
* $max: get the maximum of key values in aggregation stage
* $push: build array fo the result
* $addToSet: build array fo the result as unique keys
* $first: find the first in an aggregation query (sort)
* $last: find the last in an aggregation query (sort)