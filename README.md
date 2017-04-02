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