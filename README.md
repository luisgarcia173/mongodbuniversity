# MongoDB University #

MongoDB for Java Developers

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

![Screen Shot 2017-03-24 at 2.21.36 AM.png](https://bitbucket.org/repo/x8AeKKA/images/3300363125-Screen%20Shot%202017-03-24%20at%202.21.36%20AM.png)

*** Progress ***

![Screen Shot 2017-03-24 at 2.24.33 AM.png](https://bitbucket.org/repo/x8AeKKA/images/4114133479-Screen%20Shot%202017-03-24%20at%202.24.33%20AM.png)

==========================================================================================

### Week II (CRUD)###

* Creating documents
```
#!mongodb
db.<collection>.insertOne({<fields>});
db.<collection>.insertMany([{array>}]);
```
* ObjectId: _ _ _ _ | _ _ _ | _ _ | _ _ _ (12 Bytes, HEX String)
 > Date | Mac Address | PID | Counter
* Reading documents
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
* Comparison Operators: [Comparison Query Operators](https://docs.mongodb.com/manual/reference/operator/query-comparison/)
* Element Operators: [Query and Projection Operators] (https://docs.mongodb.com/manual/reference/operator/query/)
* Regex: [$regex] (https://docs.mongodb.com/manual/reference/operator/query/regex/)
* Updating documents: [Update field](https://docs.mongodb.com/manual/reference/method/db.collection.update/)
```
#!mongodb
db.<collection>.updateOne({<filter>}, { $set: {<modifyFields>}});
```