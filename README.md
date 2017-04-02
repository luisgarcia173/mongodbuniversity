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