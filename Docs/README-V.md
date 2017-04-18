### Week V (Aggregation) ###

** $group **
Grouping with SQL
```
#!sql
SELECT manufactures, count(*) FROM products GROUP BY manufactures;
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