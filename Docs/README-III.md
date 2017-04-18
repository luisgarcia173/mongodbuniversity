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
