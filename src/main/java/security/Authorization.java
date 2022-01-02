package security;

import user.Profile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Authorization {
    List<Profile> profiles = new ArrayList<>();

    private String check(String first_name, String last_name, int user_id, String username) {
//        MongoClientURI connectionString = new MongoClientURI("mongodb://host:port");
//        MongoClient mongoClient = new MongoClient(connectionString);
//        MongoDatabase database = mongoClient.getDatabase("db_name");
//        MongoCollection<Document> collection = database.getCollection("users");
//        long found = collection.count(Document.parse("{id : " + Integer.toString(user_id) + "}"));
//        if (found == 0) {
//            Document doc = new Document("first_name", first_name)
//                    .append("last_name", last_name)
//                    .append("id", user_id)
//                    .append("username", username);
//            collection.insertOne(doc);
//            mongoClient.close();
//            System.out.println("User not exists in database. Written.");
//            return "no_exists";
//        } else {
//            System.out.println("User exists in database.");
//            mongoClient.close();
//            return "exists";
//        }
//
return "";
    }


    private static void registerUser() {

    }
}