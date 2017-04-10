package course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;

public class BlogPostDAO {
    MongoCollection<Document> postsCollection;

    public BlogPostDAO(final MongoDatabase blogDatabase) {
        postsCollection = blogDatabase.getCollection("posts");
    }

    // Return a single post corresponding to a permalink
    public Document findByPermalink(String permalink) {

        // HW 3.2,  Work Here
        Document post = null;

        Document queryFilter = new Document("permalink", permalink);
        post = postsCollection.find(queryFilter).first();

        return post;
    }

    // Return a list of posts in descending order. Limit determines
    // how many posts are returned.
    public List<Document> findByDateDescending(int limit) {

        // HW 3.2,  Work Here
        // Return a list of DBObjects, each one a post from the posts collection
        List<Document> posts = postsCollection.find()
        		.sort(Sorts.descending("date"))
        		.limit(limit)
        		.into(new ArrayList<Document>());

        return posts;
    }


    public String addPost(String title, String body, List<String> tags, String username) {

        System.out.println("inserting blog entry " + title + " " + body);

        String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
        permalink = permalink.replaceAll("\\W", ""); // get rid of non alphanumeric
        permalink = permalink.toLowerCase();


        // HW 3.2, Work Here
        // Remember that a valid post has the following keys:
        // author, body, permalink, tags, comments, date, title
        //
        // A few hints:
        // - Don't forget to create an empty list of comments
        // - for the value of the date key, today's datetime is fine.
        // - tags are already in list form that implements suitable interface.
        // - we created the permalink for you above.

        // Build the post object and insert it
        Document post = new Document("author", username)
        	.append("body", body)
        	.append("permalink", permalink)
        	.append("tags", tags)
        	.append("comments", new ArrayList<Document>())
        	.append("date", new Date())
        	.append("title", title);

        postsCollection.insertOne(post);
        
        return permalink;
    }




    // White space to protect the innocent








    // Append a comment to a blog post
	public void addPostComment(final String name, final String email, final String body,
                               final String permalink) {
        // HW 3.3, Work Here
        // Hints:
        // - email is optional and may come in NULL. Check for that.
        // - best solution uses an update command to the database and a suitable
        //   operator to append the comment on to any existing list of comments
    	Document comment = new Document("author", name)
			 .append("email", email)
			 .append("body", body);
    	 
    	 postsCollection.updateOne(Filters.eq("permalink", permalink), Updates.push("comments", comment));
    }
}
