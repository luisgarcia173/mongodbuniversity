# MongoDB University #

MongoDB for Java Developers

### Final Workspace ###
*In progress*

### Progress ###
*In progress*

### Week I (Introduction)###

* MongoDB: Document Repository
* Install on Mac
* Json acceptable types: {object, array, string, number}
* Bson (Binary Json)
* Intro MongoDB crud operations
* Intro SparkJava
* Intro Freemarker
* Spark GET | POST
* Relational DB against Documents based
* Schema design (Embedded or not), when use it: data access is the point

*** HomeWork ***

* show dbs
* use m101
* db.hw1.findOne()
* invalid json, to assign value use (:) instead of (=), to separate attributes use (,) instead of (;)
* compile maven project, start spark on localhost:4567

==========================================================================================

### Week II (CRUD)###

* Creating documents
```
#!mongodb
db.<collection>.insertOne({<fields>});
db.<collection>.insertMany([{array>}]);
```
> ObjectId: _ _ _ _ | _ _ _ | _ _ | _ _ _ (12 Bytes, HEX String)
> Date | Mac Address | PID | Counter

* Reading documents: [Doc Reference](https://docs.mongodb.com/manual/reference/method/db.collection.find/#db.collection.find)
```
#!mongodb
db.<collection>.find({<filter>}).pretty();
db.<collection>.find({<filter>}).count();
```

* Projection
```
#!mongodb
db.<collection>.find({<filter>}, {<columnToShow>: 1, <columnToHide>: 0}).pretty();
```

* Comparison Operators: [Doc Reference](https://docs.mongodb.com/manual/reference/operator/query-comparison/)
* Element Operators: [Doc Reference](https://docs.mongodb.com/manual/reference/operator/query/)
* Regex: [Doc Reference](https://docs.mongodb.com/manual/reference/operator/query/regex/)

* Updating documents: [Doc Reference](https://docs.mongodb.com/manual/reference/method/db.collection.update/)
```
#!mongodb
db.<collection>.updateOne({<filter>}, { $set: {<modifyFields>}});
```

*** HomeWork ***
```
#!mongodb
db.grades.aggregate({'$group':{'_id':'$student_id', 'score':{'$min':'$score'}}}, {'$sort':{'_id':1}})
```

*** Extras ***

1) What query would we use in the Mongo shell to return all movies in the video.movieDetails collection that either won or were nominated for a best picture Oscar? You may assume that an award will appear in the oscars array only if the movie won or was nominated. You will probably want to create a little sample data for yourself in order to work this problem.

```
#!json
"awards" : {
    "oscars" : [
        {"award": "bestAnimatedFeature", "result": "won"},
        {"award": "bestMusic", "result": "won"},
        {"award": "bestPicture", "result": "nominated"},
        {"award": "bestSoundEditing", "result": "nominated"},
        {"award": "bestScreenplay", "result": "nominated"}
    ],
    "wins" : 56,
    "nominations" : 86,
    "text" : "Won 2 Oscars. Another 56 wins and 86 nominations."
}
```

```
#!mongodb
db.movieDetails.find({"awards.oscars.award": "bestPicture"})
```

2) Write an update command that will remove the "tomato.consensus" field for all documents matching the following criteria:

> The number of imdb votes is less than 10,000
> The year for the movie is between 2010 and 2013 inclusive
> The tomato.consensus field is null

How many documents required an update to eliminate a "tomato.consensus" field?

```
#!mongodb
db.movieDetails.updateMany({ year: {$gte: 2010, $lte: 2013},
                             "imdb.votes": {$lt: 10000},
                             $and: [{"tomato.consensus": {$exists: true} },
                                    {"tomato.consensus": null} ] },
                           { $unset: { "tomato.consensus": "" } });
```
Response:
```
#!json
{ "acknowledged" : true, "matchedCount" : 13, "modifiedCount" : 13 }
```

==========================================================================================

### Week III (Schema Design)###

MongoDB allows:

* Rich documents
* Pre join/ Embed data
* No Mongo joins
* No Mongo constraints
* Atomic operations (No Transactions)
* No declared schema

**[Mongo Design] **Which data access pattern is not well supported by the blog schema?
> Providing a table of contents by tag

**[Without Constraints] **What does Living Without Constraints refer to?
> Keeping your data consistent even though MongoDB lacks foreign key constraints

**[Without Transactions] **Which of the following operations operate atomically within a single document?
> Update, findAndModify, $addToSet(within an update), $push within an update

**[One to One] **What's a good reason you might want to keep two documents that are related to each other one-to-one in separate collections?
> To reduce the working set size of your application
> Because the combined size of the document would be larger than 16MB

**[One to Many] **When is it recommended to represent a one to many relationship in multiple collections?
> Whenever the many is large

Multikey Index:

![Screen Shot 2017-04-06 at 8.12.41 PM.png](https://bitbucket.org/repo/x8AeKKA/images/2499469043-Screen%20Shot%202017-04-06%20at%208.12.41%20PM.png)

Benefits of Embedding:

* Improve read performance
* One round trip to the DB

Trees:

* parent, ancestors, children

**Example:** Given the following typical document for a e-commerce category hierarchy collection called categories

```
#!json

{
  _id: 34,
  name: "Snorkeling",
  parent_id: 12,
  ancestors: [12, 35, 90]
}
```
Which query will find all descendants of the snorkeling category?

```
#!mongodb

db.categories.find({ancestors: 34});
```

**When to Denormalize**

* 1 x 1: Embed
* 1 x Many: Embed (from the many to one)
* Many x Many: Link

**What is an ODM?**

* [Morphia](https://mongodb.github.io/morphia/)
* Application -> ODM -> Driver -> Database
* Transparently map your Java entities to MongoDB documents and back
* Safe layer to protect application layer in order to changes on driver
* [Morphia Reference Doc](http://mongodb.github.io/morphia/1.3/getting-started/)


==========================================================================================

### Week IIII (Performance)###

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

Verbosity:
* queryPlanner (*default*): Which indexes are being used.
* executionStats (contains queryPlanner): How effective the indexes were, execution time, how many documents needed to read.
* allPlansExecution (contains queryPlanner and executionStats): All the results above and all plans available.

```
#!mongodb
var exp = db.<collection>.explain({'executionStats'});
exp.<instruction>();
```