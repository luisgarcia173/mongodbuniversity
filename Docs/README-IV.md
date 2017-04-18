### Week IV (Performance)###

**Store Engines: **The storage engine directly determines which of the following?
> the data file format and format of indexes.

**MMAP: **

* Collection
* Level
* Locking
* In Place Updates
* Power-of-two-sizes

Which of the following statements about the MMAPv1 storage engine are true?
> automatically allocates power-of-two-sized documents when new documents are inserted.
> is built on top of the mmap system call taht maps files into memory.

**Wired Tiger: **

* Document Level Concurrency
* Compression: of date / of indexed
* No Inplace Update

**Indexes: **

* Writes: Slower
* Reads: Faster

Which optimization will typically have the greatest impact on the performance of a database?
> Adding appropriate indexes on large collections so that only a small percentage of queries need to scan the collection.

**Creating Indexes**

Explain query execution:
```
#!mongodb

db.<collection>.explain().find({<filter>});
```

Explain query execution with documents involved:
```
#!mongodb

db.<collection>.explain(true).find({<filter>});
```

Creating a simple the index:
```
#!mongodb
*## order: 1 ASC, -1 DESC*
db.<collection>.createIndex({<field>: <order>});
```

Creating a composed index (Multikey):
```
#!mongodb
db.<collection>.createIndex({<field>: <order>, <field>: <order>});
```

Create Unique Index: (The column should exist in entire collection for all documents and must to exist only one value as a distinct column, because once a column not exists, its value is *NULL* than cannot be unique if more than one document hasn't the column)
```
#!mongodb
db.<collection>.createIndex({<field>: <order>}, {"unique": true});
```

Sparse Index: (Allow you to create unique index for null columns)
```
#!mongodb
db.<collection>.createIndex({<field>: <order>}, {"unique": true, "sparse": true});
```

Get existing indexes:
```
#!mongodb
db.<collection>.getIndexes();
```

Drop indexes:
```
#!mongodb
db.<collection>.dropIndex({<field>: 1});
```

What are the advantages of a sparse index?
> The index will be smaller than it would if it were not sparse.
> You can gain greater flexibility with creating Unique indexes.

Creating indexes foreground x background:
```
#!mongodb
db.<collection>.createIndex({<field>: <order>}, {"background": true});
```
![Screen Shot 2017-04-15 at 7.32.44 PM.png](https://bitbucket.org/repo/x8AeKKA/images/3299325301-Screen%20Shot%202017-04-15%20at%207.32.44%20PM.png)

**Explain**

Using explain:
```
#!mongodb
db.<collection>.explain().help();

## instruction: find, remove, update, aggregate, ...
db.<collection>.explain().<instruction>();
```

Verbosity: [Reference](https://docs.mongodb.com/manual/reference/method/cursor.explain/)
* queryPlanner (*default*): Which indexes are being used.
* executionStats (contains queryPlanner): How effective the indexes were, execution time, how many documents needed to read.
* allPlansExecution (contains queryPlanner and executionStats): All the results above and all plans available.

```
#!mongodb
var exp = db.<collection>.explain("executionStats");
exp.<instruction>();
```


** Geospatial Indexes **

Creating a 2Dimensional index: (based on vertices of a location [X, Y])
```
#!mongodb
db.<collection>.createIndex({<field>: "2d", "type": 1});
```

Querying this index: (X,Y: are the lat/long)
```
#!mongodb
db.<collection>.find({<field>: {$near : [x,y]}});
```


** Geospatial Spherical **

Draw Example:
![Screen Shot 2017-04-15 at 9.34.38 PM.png](https://bitbucket.org/repo/x8AeKKA/images/1380550004-Screen%20Shot%202017-04-15%20at%209.34.38%20PM.png)

[GoogleMap - geoApi](http://geojson.org/geojson-spec.html)

Creating 2D Sphere Index: (It's like the one above, but now means you want to use a 3d point [lat/long] and place the point into a 2d view)
```
#!mongodb
db.<collection>.createIndex({<field>: "2dsphere"});
```

Now you can use:
> $geometry and $maxDistance

Example: 
```
#!mongodb
db.stores.find({
    "loc": { 
        $near: { 
            $geometry: {
                "type": "Point",
                "coordinates": [-130, 39]
            }, 
            $maxDistance: 1000000
        }
    }
});
```


** Text Indexes **

Creating a text index, allows you to use LIKE operator by Index:
> Assumes that you have a document {"key": "text1 text2 tex3"} 

```
#!mongodb
db.<collection>.find({"key": "text1 text2 tex3"})
```
This is the only way you can find it without text index.

Now, creating the properly index:
```
#!mongodb
db.<collection>.createIndex({<field>: "text"});
```

And using a query by text index: (value uses *OR* operator, if uses several words with spaces the db will try to find all matches possible)
```
#!mongodb
db.<collection>.find({$text: {$search: "<value>"}});
```

In order to try to search by text score:
```
#!mongodb
db.<collection>.find({$text: {$search: "<value>"}}, {score: {$meta: "textScore"}}).sort({score: {$meta: "textScore" }});
```

** Efficiency of Index use **

In general, based on the preceding lecture, what is the primary factor that determines how efficiently an index can be used?
> The selectivity of the index.

In general, which of the following rules of thumb should you keep in mind when building compound indexes?
Follow the order:
1. equality field: field on which queries will perform an equality test
2. sort field: field on which queries will specify a sort
3. range field: field on which queries perform a range test

** Profilling **

Using Profiler: [Reference](https://docs.mongodb.com/manual/tutorial/manage-the-database-profiler/)

* 0: means OFF
* 1: means you want to log slow queries
* 2: means you want to log ALL queries (*debugging*)

MongoShell starting:
```
#!shellscript
mongod --profile 1 --slowms 15
```

Querying into SystemProfile:
```
#!mongodb
db.system.profile.find( { "millis" : { $gt : 1000 } } ).sort({"ts": -1})
```